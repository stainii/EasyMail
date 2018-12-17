package be.stijnhooft.easymail;

import android.app.Application;

/** Application-scoped object. Ideal for keeping reference data **/
public class EasyMailApplication extends Application {

    private static EasyMailApplication applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        //Assign a reference to this specific instance of the application
        if(applicationInstance == null){
            applicationInstance = this;
        }
    }


    //You can call this method from anywhere to get a reference to the application instance
    //you will always get a reference because in order for your component to exist, the application
    //instance MUST already exist
    public static EasyMailApplication getInstance(){
        return applicationInstance;
    }

}
