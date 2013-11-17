#include <iostream>
#include "opencv2/opencv.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv/cvaux.h"

using namespace cv;
using namespace std;

int readImageC();
int readImageCPP();

int main(int argc, char** argv) {
    return readImageC();
}
;

int readImageC() {

    IplImage * img = 0;

    img = cvLoadImage("/Users/nico/Documents/workspace-git-pai/DisplayImage/cameraman.jpg", CV_LOAD_IMAGE_UNCHANGED);
    if (!img) {
        cerr << "failed to load input image" << endl;
        return -1;
    }

    IplImage *dest = cvCreateImage(cvSize(img->width, img->height), img->depth, img->nChannels );
    //    IplImage *temp = cvCreateImage(cvSize(img->width,img->height),8,3);
    cvErode(img, dest, NULL, 2);

    cvNamedWindow("Original Image");

//    namedWindow("Display Image", CV_WINDOW_AUTOSIZE);
    cvShowImage("Original Image", img);
    waitKey(0);

    cvNamedWindow("Destination Image");
    cvShowImage("Destination Image", dest);
    waitKey(0);

    // Write the image to a file with a different name,
    // using a different image format -- .png instead of .jpg
    if (!cvSaveImage("my_image_copy.png", img)) {
        fprintf(stderr, "failed to write image file\n");
    }

    // Remember to free image memory after using it!
    cvReleaseImage(&img);

    return 0;

}
;

int readImageCPP() {

    Mat img = imread("/Users/nico/Documents/workspace-git-pai/DisplayImage/cameraman.jpg");

    if (img.data == 0) {
        cerr << "Image not found!" << endl;
        return -1;
    }
    namedWindow("image", CV_WINDOW_AUTOSIZE);
    imshow("image", img);
    waitKey();
    return 0;
}
;

