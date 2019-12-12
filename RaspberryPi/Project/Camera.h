#ifndef FINGER_CAMERA_H
#define FINGER_CAMERA_H

#include <vector>
#include <opencv2/core/mat.hpp>
#include <future>
#include <opencv2/face.hpp>

class Camera {
private:
    void loadComparisonData(std::vector<cv::Mat> &images, std::vector<int>& labels);
    void loadData(std::vector<cv::Mat> &images, std::vector<int> &labels);
    void run(std::vector<cv::Mat> images, std::vector<int> labels, cv::Ptr<cv::face::FaceRecognizer> model);
    void deleteAll();
    void addUser(const std::string &id);
    std::string readLine();
    void createSocket();
    void photoTaken();

public:
    Camera();
    void deleteUser(std::string id);
};

#endif //FINGER_CAMERA_H
