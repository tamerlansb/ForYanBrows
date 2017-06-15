#include <jni.h>
#include <string>
#include <vector>
#include <algorithm>
#include <cctype>

#include "include/CkHttp.h"
#include "json.hpp"

const int MAX_COUNT_TIPS = 5;

using namespace std;
using json = nlohmann::json;

bool stringIsEmpty(string s)
{
    int counter = 0;
    for (int i = 0; i < s.size(); ++i)
    {
        if (isspace(s[i]))
            counter++;
    }
    return counter == s.size();
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_example_admin_foryanbrows_AutoCompleteAdapter_GetTips(
        JNIEnv *env,
        jobject obj, jstring param) {
    string response ;
    jobjectArray result;
    jboolean  isCopy;
    CkHttp http;
    result = (jobjectArray)env->NewObjectArray(0,
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
            response = "";
        }
        else
        {
            response = string(str);
            json j = json::parse(response);
            int i = 1;
            for (auto elem: j) {
                if (i == 1) {
                        i++;
                } else
                if (i == 2) {
                    int counter = 0;
                    vector<string> tips;
                    for (string e: elem) {
                        if ( string(e) != ""  && find(tips.begin(), tips.end(), string(e) ) == tips.end() &&
                                !stringIsEmpty(string(e))) {
                            tips.push_back(e);
                            ++counter;
                        }
                        if (counter == MAX_COUNT_TIPS)
                            break;
                    }
                    result = (jobjectArray)env->NewObjectArray(tips.size(),
                                                               env->FindClass("java/lang/String"),
                                                               env->NewStringUTF(""));
                    for (int k = 0; k < tips.size(); ++k) {
                        env->SetObjectArrayElement(
                                result, k , env->NewStringUTF(tips[k].c_str() ));
                    }
                    break;
                }
            }
        }
    }
    catch (...)
    {
        result = (jobjectArray)env->NewObjectArray(0,
                                                   env->FindClass("java/lang/String"),
                                                   env->NewStringUTF(""));
    }
    //env->NewStringUTF(response.c_str());
    return result;
}
