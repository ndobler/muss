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
void test4Watershed_b();
int prepareMarkers(IplImage* markers_8, IplImage markers_32);
double granulometry(IplImage* img, int shape, int numberOfIterations);
void showImage(IplImage* image);

int main(int argc, char** argv) {
//    const char* imgName= "/Users/nico/Downloads/TestImages/cameraman.png";
//    const char* imgName = "/Users/nico/Downloads/TestImages/coffee_grains.jpg";
//    const char* imgName= "/Users/nico/Downloads/TestImages/hitchcock.png";
//    const char* imgName= "/Users/nico/Downloads/TestImages/Lenna.png";
//    const char* imgName= "/Users/nico/Dropbox/academia/Procesamiento Avanzado de Imagenes/TestImages/particles.png";
//    const char* imgName= "/Users/nico/Downloads/TestImages/plat.jpg";
//    const char* imgName= "/Users/nico/Downloads/TestImages/wheel.png";
//    const char* imgName= "/Users/nico/Downloads/TestImages/white_grains.jpg";
//    IplImage *img = cvLoadImage(imgName, CV_LOAD_IMAGE_UNCHANGED);
    /*IplImage* marker = cvLoadImage(
     "/Users/nico/Dropbox/academia/Procesamiento Avanzado de Imagenes/TestImages/coffee_markers.png",
     CV_LOAD_IMAGE_UNCHANGED);
     cvNamedWindow("Original");
     cvShowImage("Original", marker);
     cvWaitKey(0);
     */
    // boundaryExtraction(img);
    //myWatershed(img);
    //customSEs(img);
    // usingWatershed(img);
    //test4Watershed_b();
    // waitKey(0);
    //  cvDestroyAllWindows();
    //cvReleaseImage(&img);
    const char* imgName = "/Users/nico/Dropbox/academia/Procesamiento Avanzado de Imagenes/TestImages/particles.png";
    IplImage* img = cvLoadImage(imgName, CV_LOAD_IMAGE_GRAYSCALE);
    int imgWidth = cvGetSize(img).width;
    int i = 1;

    while (i < 10) {
        cout << "RECT" << i << " size: " << granulometry(img, CV_SHAPE_RECT, i++) << "\n";
    }
    i = 1;
    while (i < 15) {
        cout << "Cross" << i << " size: " << granulometry(img, CV_SHAPE_CROSS, i++) << "\n";
    }
    i = 1;
    while (i < 10) {
        cout << "ELLIPSE" << i << " size: " << granulometry(img, CV_SHAPE_ELLIPSE, i++) << "\n";
    }
    return 0;
}
;

double granulometry(IplImage* img, int shape, int numberOfIterations) {

    IplImage* destinationImage = cvCreateImage(cvGetSize(img), IPL_DEPTH_8U, 1);
    IplImage* tempImage = cvCreateImage(cvGetSize(img), IPL_DEPTH_8U, 1);
    double volumeoriginalImage = cvCountNonZero(img);

    IplConvKernel* se = cvCreateStructuringElementEx(2 * numberOfIterations + 1, 2 * numberOfIterations + 1,
            numberOfIterations, numberOfIterations, shape);
    cvMorphologyEx(img, destinationImage, tempImage, se, CV_MOP_OPEN);
    double volumeOpenedImage = cvCountNonZero(destinationImage);
    double ni = (double) 1 - volumeOpenedImage / volumeoriginalImage;

    se = cvCreateStructuringElementEx(2 * (numberOfIterations + 1) + 1, 2 * (numberOfIterations + 1) + 1,
            numberOfIterations, numberOfIterations, shape);
    cvMorphologyEx(img, destinationImage, tempImage, se, CV_MOP_OPEN);
    volumeOpenedImage = cvCountNonZero(destinationImage);
    double niplus1 = (double) 1 - volumeOpenedImage / volumeoriginalImage;

    double g = niplus1 - ni;
    return g;

}

int prepareMarkers(IplImage* markers_8, IplImage markers_32) {
    int comp_count = 0;   // numero de regiones finales;
    CvSeq* contours = 0;
    CvMemStorage* storage = cvCreateMemStorage(0);

    cvFindContours(markers_8, storage, &contours, sizeof(CvContour), CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE);
    cvZero(&markers_32);
    for (; contours != 0; contours = contours->h_next, comp_count++) {
        cvDrawContours(&markers_32, contours, cvScalarAll(comp_count + 1), cvScalarAll(comp_count + 1), -1, -1, 8,
                cvPoint(0, 0));
    }

    return comp_count;
}

IplImage* paintWshed(IplImage *src, IplImage *markers, int comp_count) {
    printf("%i", comp_count);
    CvRNG rng = cvRNG(-1);
    CvMat* color_tab = cvCreateMat(1, comp_count, CV_8UC3);
    for (int i = 0; i < comp_count; i++) {
        uchar* ptr = color_tab->data.ptr + i * 3;
        ptr[0] = (uchar) (cvRandInt(&rng) % 180 + 50);
        ptr[1] = (uchar) (cvRandInt(&rng) % 180 + 50);
        ptr[2] = (uchar) (cvRandInt(&rng) % 180 + 50);
    }

    IplImage* wshed = cvCloneImage(src);
    cvZero(wshed);

    // paint the watershed image (only part of 32bit image)
    for (int i = 0; i < markers->height; i++)
        for (int j = 0; j < markers->width; j++) {
            int idx = CV_IMAGE_ELEM(markers, int, i, j);
            uchar* dst = &CV_IMAGE_ELEM(wshed, uchar, i, j * 3);
            if (idx == -1)
                dst[0] = dst[1] = dst[2] = (uchar) 255;
            else if (idx <= 0 || idx > comp_count)
                dst[0] = dst[1] = dst[2] = (uchar) 0; // should not get here
            else {
                uchar* ptr = color_tab->data.ptr + (idx - 1) * 3;
                dst[0] = ptr[0];
                dst[1] = ptr[1];
                dst[2] = ptr[2];
            }
        }

    return wshed;
}

void showImage(IplImage* image) {
    cvNamedWindow("Image");
    cvShowImage("Image", image);
    cvWaitKey(0);
}

IplImage* createMarkers(IplImage* origin) {
    IplImage* destImg = cvCreateImage(cvGetSize(origin), IPL_DEPTH_8U, 1);
    IplImage* origingrayscale = cvCreateImage(cvGetSize(origin), IPL_DEPTH_8U, 1);
    cvCvtColor(origin, origingrayscale, CV_RGB2GRAY);
    cvThreshold(origingrayscale, destImg, 200, 255., CV_THRESH_TOZERO);
    cvNot(destImg, destImg);

    IplImage* temp = cvCloneImage(destImg);
    cvMorphologyEx(destImg, destImg, temp, NULL, CV_MOP_OPEN, 3);
    showImage(destImg);
    cvErode(destImg, destImg, NULL, 9);
    showImage(destImg);
    cvThreshold(destImg, destImg, 250, 255., CV_THRESH_TOZERO);
    showImage(destImg);

    return destImg;
}
;

void test4Watershed_b() {
    const char* imgName = "/Users/nico/Dropbox/academia/Procesamiento Avanzado de Imagenes/TestImages/coffee_grains.jpg";
    IplImage* src = cvLoadImage(imgName, CV_LOAD_IMAGE_UNCHANGED);
    IplImage* gradient = cvCloneImage(src);
    IplImage* tmp = cvCloneImage(src);

    // creo el gradiente
    cvMorphologyEx(src, gradient, tmp, NULL, CV_MOP_GRADIENT, 1);
    //cvNamedWindow("Gradient");
    //cvShowImage("Gradient", gradient);
    //cvWaitKey(0);

    IplImage* img2 = createMarkers(src);
    //IplImage* img2 = cvLoadImage("/Users/nico/Dropbox/academia/Procesamiento Avanzado de Imagenes/TestImages/coffee_markers.png", CV_LOAD_IMAGE_UNCHANGED);
    //cvNot(img2, img2);  //markers se necesitan en blanco
    //IplImage* markers_gray = cvCreateImage(cvGetSize(img2), IPL_DEPTH_8U, 1);
    //cvCvtColor(img2, markers_gray, CV_RGB2GRAY); //convierte de rgb a escala de grises en un canal
    IplImage* markers_gray = img2;

    IplImage* markers = cvCreateImage(cvGetSize(img2), IPL_DEPTH_32S, 1);
    //reescala la imagen en resolucion porque el watershed necesita una imagen de 32bit de resolucion para sus movidas
    cvScale(markers_gray, markers, 255., 0.0);

    int comp_count = prepareMarkers(markers_gray, *markers);
    //cvNamedWindow("Markers");
    //cvShowImage("Markers", markers);
    //cvWaitKey(0);

    //aplico el watershed de la imagen original usando la de markers que genere, el watershed lo genera en markers
    cvWatershed(src, markers);

    //cvNamedWindow("T");
    //cvShowImage("T",markers);
    //cvWaitKey(0);

    //pinto cada zona con un color distinto
    img2 = paintWshed(src, markers, comp_count);

    cvNamedWindow("Watershed transform");
    cvShowImage("Watershed transform", img2);
    cvWaitKey(0);

    //combino las dos imagenes haciendo una suma "ponderada"
    cvAddWeighted(img2, 0.5, src, 0.5, 0, img2);

    cvShowImage("Watershed transform", img2);
    cvWaitKey(0);

}

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

