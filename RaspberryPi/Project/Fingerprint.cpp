#include <wiringPi.h>
#include <wiringSerial.h>
#include <iostream>
#include <termios.h>
#include <poll.h>

#include "Fingerprint.h"
#include "Camera.h"

int deviceId;
struct pollfd src{};

char Fingerprint::calculateChecksum(unsigned char value[]) {
    for (int i = 1; i < 6; i++) {
        value[6] = value[6] ^ value[i];
    }
    return value[6];
}

void Fingerprint::showResult(bool success) {
    int led;
    if (success) {

        led = 28;
    } else {
        led = 29;
    }

    for (int i = 0; i < 3; ++i) {
        digitalWrite(led, 1);
        delay(500);
        digitalWrite(led, 0);
        delay(500);
    }
}

void Fingerprint::idConverter(std::string id, unsigned char & byte1, unsigned char & byte2) {
    int intId = atoi(id.c_str());
    byte1 = (intId & 0x0000ff00) >> 8;
    byte2 = intId & 0x000000ff;
}

void Fingerprint::addFinger(unsigned char attempt, std::string id) {
    unsigned char command[] = {0xF5, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0xF5};
    idConverter(id, command[2], command[3]);
    command[1] = attempt;
    command[6] = calculateChecksum(command);

    serialPutchar(deviceId, command[0]);
    serialPutchar(deviceId, command[1]);
    serialPutchar(deviceId, command[2]);
    serialPutchar(deviceId, command[3]);
    serialPutchar(deviceId, command[4]);
    serialPutchar(deviceId, command[5]);
    serialPutchar(deviceId, command[6]);
    serialPutchar(deviceId, command[7]);

    poll(&src, 1, -1);

    delay(100);

    int bytesReceived = serialDataAvail(deviceId);
    unsigned char answer[8];
    for (int j = 0; j < bytesReceived; ++j) {
        answer[j] = serialGetchar(deviceId);
    }

    if (answer[4] == 0x00) {
        if (answer[1] < 3) {
            addFinger(attempt + 1, id);
        } else {
            std::cout << "added_" << id << std::endl;
            showResult(true);
        }
    } else {
        std::cout << "failedadd_" << id << std::endl;
        camera->deleteUser(id);
        showResult(false);
    }
}


void Fingerprint::compareFinger(std::string id) {
    unsigned char command[] = {0xF5, 0x0B, 0x00, 0x00, 0x00, 0x00, 0x00, 0xF5};
    idConverter(id, command[2], command[3]);
    command[6] = calculateChecksum(command);

    serialPutchar(deviceId, command[0]);
    serialPutchar(deviceId, command[1]);
    serialPutchar(deviceId, command[2]);
    serialPutchar(deviceId, command[3]);
    serialPutchar(deviceId, command[4]);
    serialPutchar(deviceId, command[5]);
    serialPutchar(deviceId, command[6]);
    serialPutchar(deviceId, command[7]);

    poll(&src, 1, -1);

    delay(100);

    int bytesReceived = serialDataAvail(deviceId);

    unsigned char answer[8];
    for (int j = 0; j < bytesReceived; ++j) {
        answer[j] = serialGetchar(deviceId);
    }

    if (answer[4] == 0x00) {
        open(id);
        showResult(true);
    } else {
        std::cout << "failedcompare_" << id << std::endl;
        showResult(false);
    }
}

void Fingerprint::deleteAll() {
    unsigned char command[] = {0xF5, 0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0xF5};
    command[6] = calculateChecksum(command);

    serialPutchar(deviceId, command[0]);
    serialPutchar(deviceId, command[1]);
    serialPutchar(deviceId, command[2]);
    serialPutchar(deviceId, command[3]);
    serialPutchar(deviceId, command[4]);
    serialPutchar(deviceId, command[5]);
    serialPutchar(deviceId, command[6]);
    serialPutchar(deviceId, command[7]);

    poll(&src, 1, -1);

    delay(100);

    int bytesReceived = serialDataAvail(deviceId);

    unsigned char answer[8];
    for (int j = 0; j < bytesReceived; ++j) {
        answer[j] = serialGetchar(deviceId);
    }

    if (answer[4] == 0x00) {
        std::cout << "deletedall" << std::endl;
        showResult(true);
    } else {
        showResult(false);
    }
}


void Fingerprint::deleteUser(std::string id) {
    unsigned char command[] = {0xF5, 0x04, 0x00, 0x00, 0x01, 0x00, 0x00, 0xF5};
    idConverter(id, command[2], command[3]);
    command[6] = calculateChecksum(command);

    serialPutchar(deviceId, command[0]);
    serialPutchar(deviceId, command[1]);
    serialPutchar(deviceId, command[2]);
    serialPutchar(deviceId, command[3]);
    serialPutchar(deviceId, command[4]);
    serialPutchar(deviceId, command[5]);
    serialPutchar(deviceId, command[6]);
    serialPutchar(deviceId, command[7]);

    poll(&src, 1, -1);

    delay(100);

    int bytesReceived = serialDataAvail(deviceId);
    unsigned char answer[8];
    for (int j = 0; j < bytesReceived; ++j) {
        answer[j] = serialGetchar(deviceId);
    }

    if (answer[4] == 0x00) {
        std::cout << "deleted_" << id << std::endl;
        showResult(true);
    }
}

void Fingerprint::open(std::string id) {
    std::cout << "opened_" << id << std::endl;
    digitalWrite(26, 1);
    delay(3000);
    digitalWrite(26, 0);
}

void Fingerprint::manualOpen() {
    std::cout << "opened" << std::endl;
    digitalWrite(26, 1);
    delay(3000);
    digitalWrite(26, 0);
}

Fingerprint::Fingerprint(Camera *camera) {
    this->camera = camera;

    deviceId = serialOpen("/dev/ttyS0", 19200);

    struct termios options{};
    tcgetattr(deviceId, &options);   // Read current options
    options.c_cflag &= ~CSIZE;  // Mask out size
    options.c_cflag |= CS8;     // Or in 7-bits
    options.c_cflag &= ~PARENB;  // Enable Parity - even by default
    options.c_cflag &= ~CSTOPB;  // 1 stop bit
    options.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);
    tcsetattr(deviceId, 0, &options);   // Set new options

    src = {
            deviceId,
            POLLIN,
            0
    };
}
