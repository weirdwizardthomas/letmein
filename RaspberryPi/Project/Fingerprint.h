#ifndef LOCKBOX_FINGERPRINT_H
#define LOCKBOX_FINGERPRINT_H

#include "Camera.h"

class Fingerprint {
private:
    Camera *camera;
    void open(std::string id);
    void idConverter(std::string id, unsigned char & byte1, unsigned char & byte2);
    char calculateChecksum(unsigned char value[]);

public:
    Fingerprint(Camera *camera);
    void deleteAll();
    void deleteUser(std::string id);
    void addFinger(unsigned char attempt, std::string id);
    void compareFinger(std::string id);
    void manualOpen();
    void showResult(bool success);
};


#endif //LOCKBOX_FINGERPRINT_H
