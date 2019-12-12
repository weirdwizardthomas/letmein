#include <wiringPi.h>
#include <iostream>
#include "Camera.h"

int lock = 26;
int redLED = 27;
int greenLED = 28;
int button = 29;

unsigned int firstPress = 0;

void buttonPressed(){
    if(firstPress + 1000 < millis()){
        firstPress = millis();
        std::cout << "doorbell" << std::endl;
    }
}

int main(int argc, const char *argv[]) {
    wiringPiSetup();
    pinMode(greenLED, OUTPUT);
    pinMode(redLED, OUTPUT);
    pinMode(lock, OUTPUT);
    pinMode(button, INPUT);

    wiringPiISR(button, INT_EDGE_RISING, buttonPressed);

    new Camera();
}

