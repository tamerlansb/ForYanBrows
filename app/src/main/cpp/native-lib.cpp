#include <jni.h>
#include <string>

#include "include/CkHttp.h"
#include "include/CkJsonObject.h"
#include "json.hpp"

using namespace std;
using json = nlohmann::json;
extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_example_admin_foryanbrows_MainActivity_GetTips(
        JNIEnv *env,
        jobject obj, jstring param) {
    string response ;
    jobjectArray result;
    jboolean  isCopy;
    CkHttp http;

    result = (jobjectArray)env->NewObjectArray(5,
                                               env->FindClass("java/lang/String"),
                                               env->NewStringUTF(""));
    try {
        bool success = http.UnlockComponent("Anything for 30-day trial");
        if (success != true) {
            response = string(http.lastErrorText()) + "\r\n";
        }
        else
        {
            response = "success";
        }

        string addr = "http://suggest.yandex.ru/suggest-ff.cgi?part=";
        addr += env->GetStringUTFChars(param, &isCopy);
        const char *str = http.quickGetStr(addr.c_str());

        if (str==NULL) {
            response = "Null";
        } else
        {
            response = string(str);

			for(int i=0;i<5;i++) {
                env->SetObjectArrayElement(
                        result, i, env->NewStringUTF( string("qQ").c_str() ));
            }
        }



        string request_word = env->GetStringUTFChars(param, &isCopy);
        //env->SetObjectArrayElement(
        //        result, 4, env->NewStringUTF( response.c_str() ));
        env->SetObjectArrayElement(
                result, 3, env->NewStringUTF( request_word.c_str() ));
        env->SetObjectArrayElement(
                result, 0, env->NewStringUTF( "Success " ));
        /*CkJsonObject json;
        success = json.LoadFile("http://suggest.yandex.ru/suggest-ff.cgi?part=янд");
        if (success != true) {
            hello = string(http.lastErrorText()) + "\r\n";
        }
        else
        {
            json[]
            hello = "success json";
        }*/
        //hello = string(str);
        /*if (http.get_LastMethodSuccess() != true) {
            hello = string(http.lastErrorText());
        } else {
            hello = string(str);
        }*/
		
    }
    catch (...)
    {
        response = "exeption";
        env->SetObjectArrayElement(
                result, 3, env->NewStringUTF( "exeption"  ));
    }
    //env->NewStringUTF(response.c_str());
    return result;
}
