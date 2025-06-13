cmake_minimum_required(VERSION 3.22.1)

project("tetris")

file(GLOB HAL_SOURCES "hal/*.c")
add_library( # Sets the name of the library.
        tetris

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp)

find_library(log-lib log)

target_include_directories(${CMAKE_PROJECT_NAME} PRIVATE hal)

target_link_libraries(${CMAKE_PROJECT_NAME}
        ${log-lib}
)