TARGET = mushrooms
TEMPLATE = app
INCLUDEPATH += .
QT += declarative
CONFIG += qt-components

symbian {
    # To lock the application to landscape orientation
    LIBS += -lcone -leikcore -lavkon

    RESOURCES += \
        res.qrc

    INCLUDEPATH += MW_LAYER_SYSTEMINCLUDE
    LIBS += -L/epoc32/release/armv5/lib -lremconcoreapi
    LIBS += -L/epoc32/release/armv5/lib -lremconinterfacebase

    TARGET.CAPABILITY += NetworkServices
    ICON = desktop/$${TARGET}.svg

    TARGET.UID3 = 0x2006179e

    TARGET.EPOCHEAPSIZE = 0x100000 0x2000000

    DEPLOYMENT.display_name = Грибы
    VERSION = 1.1.0

    img.sources = qml/images
    img.path += .
    DEPLOYMENT += img

    thumb.sources = qml/thumbnails
    thumb.path += .
    DEPLOYMENT += thumb

    qmls.sources = $${OTHER_FILES}
    qmls.path += qml
    DEPLOYMENT += qmls

    vendorinfo = "%{\"FRUCT Lab in IT-park of PetrSU\"}" ":\"FRUCT Lab in IT-park of PetrSU\""

    my_deployment.pkg_prerules = vendorinfo
    DEPLOYMENT += my_deployment
}

TRANSLATIONS += t1_ru.ts

# The .cpp file which was generated for your project. Feel free to hack it.
SOURCES += main.cpp \
    imagewithborder.cpp

IMAGES_FILES = qml/images/*.jpg
THUMB_FILES = qml/thumbnails/*.jpg

OTHER_FILES = \
    qml/*.js \
    qml/*.qml \
    qml/*.xml

RESOURCES += \
    res.qrc

HEADERS += \
    imagewithborder.h
