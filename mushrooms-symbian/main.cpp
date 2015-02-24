#include <QtGui/QApplication>
#include <QtDeclarative>
#include <imagewithborder.h>

// Lock orientation in Symbian
#ifdef Q_OS_SYMBIAN
#include <eikenv.h>
#include <eikappui.h>
#include <aknenv.h>
#include <aknappui.h>
#endif

class TranslationTest : public QObject
{
    Q_OBJECT
    Q_PROPERTY(QString emptyString READ getEmptyString NOTIFY languageChanged)
public:
    TranslationTest()
    {
        translator1 = new QTranslator(this);
    }

    QString getEmptyString()
    {
        return "";
    }

    Q_INVOKABLE void selectLanguage(QString language) {
        if(language == QString("ru")) {
            translator1->load("translations/t1_ru", ":/");
            qApp->installTranslator(translator1);
        }
        if(language == QString("en")){
            qApp->removeTranslator(translator1);
        }
        emit languageChanged(language);
    }
signals:
    void languageChanged(QString language);
private:
    QTranslator *translator1;
};


Q_DECL_EXPORT int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    TranslationTest myObj;
    QDeclarativeView view;
    QString locale = QLocale::system().name().split("_").first();
    view.rootContext()->setContextProperty("systemLocale", locale);

    app.setApplicationName("Mushrooms");
    app.setApplicationVersion("1.1.0");

    // prefix of path to images
    QString appPath = QApplication::applicationDirPath();
    view.rootContext()->setContextProperty("applicationPath", "file:///" + appPath + "/");

    qmlRegisterType<ImageWithBorder>("mushrooms", 1, 0, "ImageWithBorder");

    view.rootContext()->setContextProperty("rootItem", (QObject *)&myObj);

#ifdef MEEGO_EDITION_HARMATTAN
    view.setSource(QUrl(QString(DATAPREFIX).append("/mushrooms/qml/main.qml")));
#else
    view.setSource(QUrl("../../../qml/main.qml"));
#endif

#ifdef Q_OS_SYMBIAN
    // Lock orientation to landscape in Symbian
    CAknAppUi* appUi = dynamic_cast<CAknAppUi*> (CEikonEnv::Static()->AppUi());
    TRAP_IGNORE(
                if (appUi)
                appUi->SetOrientationL(CAknAppUi::EAppUiOrientationPortrait);
            )
#endif

    view.showFullScreen();

    return app.exec();
}

#include "main.moc"
