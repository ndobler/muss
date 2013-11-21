#include <iostream>
#include "opencv2/opencv.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv/cvaux.h"

using namespace cv;
using namespace std;

int readImageC();
int readImageCPP();
int boundaryExtraction(const IplImage*);
int myWatershed(const IplImage*);
void customSEs(const IplImage *img);
int usingWatershed(const IplImage* img);

int main(int argc, char** argv) {
//    const char* imgName= "/Users/nico/Downloads/TestImages/cameraman.png";
    const char* imgName = "/Users/nico/Downloads/TestImages/coffee_grains.jpg";
//    const char* imgName= "/Users/nico/Downloads/TestImages/hitchcock.png";
//    const char* imgName= "/Users/nico/Downloads/TestImages/Lenna.png";
//    const char* imgName= "/Users/nico/Downloads/TestImages/particles.png";
//    const char* imgName= "/Users/nico/Downloads/TestImages/plat.jpg";
//    const char* imgName= "/Users/nico/Downloads/TestImages/wheel.png";
//    const char* imgName= "/Users/nico/Downloads/TestImages/white_grains.jpg";
    IplImage *img = cvLoadImage(imgName, CV_LOAD_IMAGE_UNCHANGED);
    cvNamedWindow("Original");
    cvShowImage("Original", img);

    // boundaryExtraction(img);
    //myWatershed(img);
    //customSEs(img);
    usingWatershed(img);

    waitKey(0);
    cvDestroyAllWindows();
    cvReleaseImage(&img);
    return 0;
}
;

int usingWatershed(const IplImage* img) {
    IplImage *markers = cvLoadImage("/Users/nico/Downloads/TestImages/coffee_markers.png", CV_LOAD_IMAGE_UNCHANGED);
    IplImage *markersConv = cvCreateImage(cvSize(markers->width, markers->height), markers->depth, 1);
    cvConvertImage(markers, markersConv, CV_32S);
    cvCvtColor(markers, markersConv, CV_RGB2GRAY);

    cvWatershed(img, markersConv);
    cvNamedWindow("WS"), cvShowImage("WS", markersConv);
    cvReleaseImage(&markersConv);

    return 0;
}
;

int myWatershed(const IplImage *img) {
    IplImage *dest = cvCreateImage(cvSize(img->width, img->height), img->depth, img->nChannels);
    IplImage *temp = cvCreateImage(cvSize(img->width, img->height), img->depth, img->nChannels);
    cvMorphologyEx(img, dest, temp, NULL, CV_MOP_GRADIENT);

    cvNamedWindow("Watershed"), cvShowImage("Watershed", dest);
    cvReleaseImage(&dest);
    cvReleaseImage(&temp);

    return 0;

}

void customSEs(const IplImage *img) {
    int eeGraph[5][5] =
            { { 1, 1, 1, 1, 1 }, { 1, 1, 1, 1, 0 }, { 1, 1, 1, 0, 0 }, { 1, 1, 0, 0, 0 }, { 1, 0, 0, 0, 0 } };

    IplConvKernel* se = cvCreateStructuringElementEx(5, 5, 2, 2, CV_SHAPE_CUSTOM, *eeGraph);

    IplImage *imgDest = cvCreateImage(cvSize(img->width, img->height), img->depth, img->nChannels);
    cvErode(img, imgDest, se);
    const char* nameW1 = "Con un triangulo";
    cvNamedWindow(nameW1);
    cvShowImage(nameW1, imgDest);

    IplImage *imgDest2 = cvCreateImage(cvSize(img->width, img->height), img->depth, img->nChannels);
    cvErode(img, imgDest2);
    const char* nameW2 = "Por defecto";
    cvNamedWindow(nameW2);
    cvShowImage(nameW2, imgDest2);

    cvReleaseImage(&imgDest);
    cvReleaseImage(&imgDest2);

    IplImage *imgDest3 = cvCreateImage(cvSize(img->width, img->height), img->depth, img->nChannels);
    cvCvtColor(img, imgDest3, CV_RGB2BGR);
    const char* nameW3 = "Color convert";
    cvNamedWindow(nameW3);
    cvShowImage(nameW3, imgDest3);
}

int boundaryExtraction(const IplImage *img) {
    IplImage *imgEroded = cvCreateImage(cvSize(img->width, img->height), img->depth, img->nChannels);
    cvErode(img, imgEroded);
    IplImage *destImage = cvCreateImage(cvSize(img->width, img->height), img->depth, img->nChannels);
    cvAbsDiff(img, imgEroded, destImage);

    cvNamedWindow("Boundary");
    cvShowImage("Boundary", destImage);

    cvReleaseImage(&imgEroded);
    cvReleaseImage(&destImage);

    return 0;
}
;

int readImageC() {

    IplImage * img = 0;

    img = cvLoadImage("/Users/nico/Documents/workspace-git-pai/DisplayImage/cameraman.jpg", CV_LOAD_IMAGE_UNCHANGED);
    if (!img) {
        cerr << "failed to load input image" << endl;
        return -1;
    }

    IplImage *dest = cvCreateImage(cvSize(img->width, img->height), img->depth, img->nChannels);
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

