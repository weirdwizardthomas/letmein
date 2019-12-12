cmake_minimum_required(VERSION 3.7)
project(Project2)
set(CMAKE_CXX_STANDARD 14)
add_executable(${PROJECT_NAME} main.cpp Camera.cpp Camera.h Fingerprint.cpp Fingerprint.h)

SET(CMAKE_CXX_FLAGS "-lwiringPi")
find_library(wiringPi_LIB wiringPi)
set(CMAKE_CXX_STANDARD 14)
target_link_libraries(${PROJECT_NAME} ${wiringPi_LIB})

find_package( OpenCV REQUIRED )
include_directories( ${OpenCV_INCLUDE_DIRS} )
link_directories( ${OpenCV_LIB_DIR} )
target_link_libraries( ${PROJECT_NAME} ${OpenCV_LIBS} )