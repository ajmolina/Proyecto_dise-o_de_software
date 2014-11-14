package com.jdmaestre.uninorte.barranquilla2go;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Jose on 06/11/2014.
 */
public class Barranquilla2GoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "plTVDaHSADaYUfJPqAljbmvw1yPKxeXpenFX4UCE", "bg7jVhG29Nq6RsIY8eHCk8KkEXaGWtCJjadQjMDh");

    }

}
