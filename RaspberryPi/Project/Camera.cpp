#include "Camera.h"
#include "Fingerprint.h"

#include <opencv2/opencv.hpp>
#include <opencv2/highgui.hpp>
#include <opencv2/imgproc/imgproc_c.h>
#include <opencv2/core/core.hpp>
#include <opencv2/face.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/objdetect/objdetect.hpp>

#include <iostream>
#include <wiringPi.h>
#include <zconf.h>
#include <sys/stat.h>
#include <sys/poll.h>
#include <dirent.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

Fingerprint *fingerprint;
int cameraId = 0;
float minConfidence = 500;
int sockfd;
struct sockaddr_in servaddr;

cv::VideoCapture *videoCapture;
cv::CascadeClassifier *haar_cascade;

void Camera::photoTaken() {
    int led = 28;
    digitalWrite(led, 1);
    delay(500);
    digitalWrite(led, 0);
    delay(500);
}


void Camera::addUser(const std::string &id) {
    std::string path = "../data/photos/" + id;
    mkdir(path.c_str(), 0755);

    cv::Mat frame;
    for (int photo = 0; photo < 10;) {
        *videoCapture >> frame;
        // Clone the current frame:
        cv::Mat original = frame.clone();
        // Convert the current frame to grayscale:
        cv::Mat gray;
        cvtColor(original, gray, CV_BGR2GRAY);
        // Find the faces in the frame:
        std::vector<cv::Rect_<int> > faces;
        //haar_cascade.detectMultiScale(gray, faces, 1.1, 50);
        (*haar_cascade).detectMultiScale(gray, faces, 1.1);

        if (!faces.empty()) {
            cv::Mat face = gray(faces[0]);
            cv::Mat face_resized;
            cv::resize(face, face_resized, cv::Size(64, 64), 1.0, 1.0, cv::INTER_CUBIC);
            std::stringstream filename;
            filename << path << "/" << photo << ".png";
            cv::imwrite(filename.str(), face_resized);
            ++photo;
            photoTaken();
            delay(500);
        }
    }
    fingerprint->addFinger(1, id);
}

void Camera::deleteAll() {
    fingerprint->deleteAll();
    system("rm -rf ../data/photos");
    mkdir("../data/photos", 0755);
}

void Camera::deleteUser(std::string id) {
    fingerprint->deleteUser(id);
    system(("rm -rf ../data/photos/" + id).c_str());
}

void Camera::loadData(std::vector<cv::Mat> &images, std::vector<int> &labels) {
    const char *PATH = "../data/photos";
    DIR *dir = opendir(PATH);
    struct dirent *entry = readdir(dir);

    while (entry != NULL) {
        if (entry->d_type == DT_DIR) {
            for (int j = 0; j < 10; ++j) {
                if (entry->d_name[0] == '.') {
                    break;
                }
                std::stringstream path;
                path << "../data/photos/" << entry->d_name << "/" << std::to_string(j) << ".png";

                cv::Mat m = cv::imread(path.str(), 1);
                cv::Mat m2;
                cvtColor(m, m2, CV_BGR2GRAY);
                images.push_back(m2);
                labels.push_back(atoi(entry->d_name));
            }

        }
        entry = readdir(dir);
    }
    closedir(dir);
}

void Camera::loadComparisonData(std::vector<cv::Mat> &images, std::vector<int> &labels) {
    for (int i = 1; i <= 40; ++i) {
        for (int j = 0; j < 10; ++j) {
            std::stringstream path;
            path << "../data/reference_photos/" << std::to_string(i) << "/" << std::to_string(j) << ".png";

            cv::Mat m = cv::imread(path.str(), 1);
            cv::Mat m2;
            cvtColor(m, m2, CV_BGR2GRAY);
            images.push_back(m2);
            labels.push_back(i);
        }
    }
}

void Camera::createSocket() {
    // create datagram socket
    sockfd = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP);
    int broadcastEnable = 1;
    int ret = setsockopt(sockfd, SOL_SOCKET, SO_BROADCAST, &broadcastEnable, sizeof(broadcastEnable));
    if (ret) {
        close(sockfd);
        return;
    }

    bzero(&servaddr, sizeof(servaddr));
    inet_pton(AF_INET, "127.0.0.1", &servaddr.sin_addr);
    servaddr.sin_port = htons(8088);
    servaddr.sin_family = AF_INET;
}

void Camera::run(std::vector<cv::Mat> images, std::vector<int> labels, cv::Ptr<cv::face::FaceRecognizer> model) {
    int im_width = images[0].cols;
    int im_height = images[0].rows;

    unsigned int firstHit = 0;
    int hits = 0;
    int lastPrediction = 0;

    std::string stringRead;

    // Holds the current frame from the Video device:
    cv::Mat frame;

    for (int j = 0;; ++j) {
        if (j == 50) {
            j = 0;
            struct pollfd pfd = {STDIN_FILENO, POLLIN, 0};
            int ret = poll(&pfd, 1, 1);
            if (ret == 1) {
                return;
            }
        }

        *videoCapture >> frame;
        std::vector<int> compression_params;
        compression_params.push_back(cv::IMWRITE_JPEG_QUALITY);

        // Convert the current frame to grayscale:
        cv::Mat gray;
        cvtColor(frame, gray, CV_BGR2GRAY);


        if (true) {
            std::vector<uchar> buff;
            cv::imencode(".jpg", frame, buff, compression_params);

            std::string packet = "image|" + std::to_string(buff.size());
            sendto(sockfd, packet.c_str(), packet.size(), 0, (const struct sockaddr *) &servaddr, sizeof(servaddr));

            int byteSent = 0;
            int actualBytesSent = 0;
            while (byteSent < buff.size()) {
                size_t toSend = std::min((size_t) 1024 * 50, buff.size() - byteSent);
                int testVar = sendto(sockfd, buff.data() + byteSent, toSend, MSG_CONFIRM,
                                     (const struct sockaddr *) &servaddr, sizeof(servaddr));
                char buffer[100];

                //recvfrom(sockfd, buffer, sizeof(buffer), 0, (struct sockaddr *) NULL, NULL);

                if (testVar == -1) {
                    perror("error");
                    delay(10);
                    continue;
                }
                actualBytesSent += testVar;
                byteSent += toSend;
            }
        }

        // Find the faces in the frame:
        std::vector<cv::Rect_<int> > faces;
        (*haar_cascade).detectMultiScale(gray, faces);

        if (faces.size() > 0) {
            cv::Mat face = gray(faces[0]);
            cv::Mat face_resized;
            cv::resize(face, face_resized, cv::Size(im_width, im_height), 1.0, 1.0, cv::INTER_CUBIC);

            // Now perform the prediction, see how easy that is:
            int prediction;
            //0 is perfect match and it goes up to infinity (closer to 0 = better prediction and confidence)
            double confidence;
            model->predict(face_resized, prediction, confidence);

            if (prediction == lastPrediction && confidence < minConfidence) {
                if (firstHit + 1000 < millis()) {
                    if (hits == 3) {
                        hits = 0;
                        fingerprint->compareFinger(std::to_string(prediction));
                    }
                } else {
                    firstHit = millis();
                    hits = 0;
                }
                ++hits;
                delay(100);
            }
            lastPrediction = prediction;
        }
    }
}

std::string Camera::readLine() {
    struct pollfd pfd = {STDIN_FILENO, POLLIN, 0};

    int ret = poll(&pfd, 1, 10);
    std::string line;
    if (ret == 1) {
        std::cin >> line;
        std::cin.ignore();
    }

    return line;
}

Camera::Camera() {
    haar_cascade = new cv::CascadeClassifier();
    videoCapture = new cv::VideoCapture();
    (*haar_cascade).load("../data/haarcascade_frontalface_default.xml");
    *videoCapture = cv::VideoCapture(cameraId);
    (*videoCapture).set(cv::CAP_PROP_BUFFERSIZE, 1);

    createSocket();
    fingerprint = new Fingerprint(this);

    std::vector<cv::Mat> images;
    std::vector<int> labels;
    cv::Ptr<cv::face::FaceRecognizer> model = cv::face::FisherFaceRecognizer::create();

    bool trained = false;

    while (true) {
        std::string line = readLine();
        std::string words[2];

        std::string delim = "_";

        auto start = 0U;
        int end = line.find(delim);

        if (end != std::string::npos) {
            words[0] = line.substr(start, end - start);
            start = end + delim.length();
            end = line.size();
            words[1] = line.substr(start, end - start);
        } else {
            words[0] = line;
        }

        if (words[0].empty()) {
            if (!trained) {
                images.clear();
                labels.clear();
                model = cv::face::FisherFaceRecognizer::create();
                loadData(images, labels);
                loadComparisonData(images, labels);
                model->train(images, labels);
                fingerprint->showResult(true);
                trained = true;
            }
            run(images, labels, model);
        } else if (words[0].compare("deleteall") == 0) {
            trained = false;
            deleteAll();
        } else if (words[0].compare("delete") == 0) {
            trained = false;
            deleteUser(words[1]);
        } else if (words[0].compare("add") == 0) {
            trained = false;
            addUser(words[1]);
        } else if (words[0].compare("open") == 0) {
            fingerprint->manualOpen();
        }
    }
}