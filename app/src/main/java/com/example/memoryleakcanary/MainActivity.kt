package com.example.memoryleakcanary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

/**
 * Simple example of memory leak detection using LeakCanary from Square
 * https://square.github.io/leakcanary/
 */
class MainActivity : AppCompatActivity() {

    private lateinit var tvHelloWorld: View
    private lateinit var app: MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHelloWorld = findViewById(R.id.tvHelloWorld)
        app = application as MyApplication

        // add a reference of the TextView (to the application class)
        // every single time the activity is recreated...
        // this is very bad and will cause a memory leak on device rotation!
        println("onCreate() - add reference to $tvHelloWorld")
        app.leakedViews.add(tvHelloWorld)

        /**
         * After rotating the emulator screen a few times, you get this message from LeakCanary!
         *
        ====================================
        HEAP ANALYSIS RESULT
        ====================================
        1 APPLICATION LEAKS

        References underlined with "~~~" are likely causes.
        Learn more at https://squ.re/leaks.

        341362 bytes retained by leaking objects
        Displaying only 1 leak trace out of 4 with the same signature
        Signature: 37c8faf8749bf070a9909da7729b168d70d42c98
        ┬───
        │ GC Root: System class
        │
        ├─ android.provider.FontsContract class
        │    Leaking: NO (MyApplication↓ is not leaking and a class is never leaking)
        │    ↓ static FontsContract.sContext
        ├─ com.example.memoryleakcanary.MyApplication instance
        │    Leaking: NO (Application is a singleton)
        │    mBase instance of android.app.ContextImpl, not wrapping known Android context
        │    ↓ MyApplication.leakedViews
        │                    ~~~~~~~~~~~
        ├─ java.util.ArrayList instance
        │    Leaking: UNKNOWN
        │    Retaining 365567 bytes in 4760 objects
        │    ↓ ArrayList.elementData
        │                ~~~~~~~~~~~
        ├─ java.lang.Object[] array
        │    Leaking: UNKNOWN
        │    Retaining 365547 bytes in 4759 objects
        │    ↓ Object[].[0]
        │               ~~~
        ├─ com.google.android.material.textview.MaterialTextView instance
        │    Leaking: YES (View.mContext references a destroyed activity)
        │    Retaining 74209 bytes in 1139 objects
        │    View not part of a window view hierarchy
        │    View.mAttachInfo is null (view detached)
        │    View.mID = R.id.tvHelloWorld
        │    View.mWindowAttachCount = 1
        │    mContext instance of com.example.memoryleakcanary.MainActivity with mDestroyed = true
        │    ↓ MaterialTextView.mContext
        ╰→ com.example.memoryleakcanary.MainActivity instance
        ​     Leaking: YES (ObjectWatcher was watching this because com.example.memoryleakcanary.MainActivity received
        ​     Activity#onDestroy() callback and Activity#mDestroyed is true)
        ​     Retaining 5946 bytes in 163 objects
        ​     key = c1c69553-a31e-46da-ae1d-914472730da3
        ​     watchDurationMillis = 11502
        ​     retainedDurationMillis = 6502
        ​     mApplication instance of com.example.memoryleakcanary.MyApplication
        ​     mBase instance of androidx.appcompat.view.ContextThemeWrapper, not wrapping known Android context
        ====================================
        0 LIBRARY LEAKS

        A Library Leak is a leak caused by a known bug in 3rd party code that you do not have control over.
        See https://square.github.io/leakcanary/fundamentals-how-leakcanary-works/#4-categorizing-leaks
        ====================================
        METADATA

        Please include this in bug reports and Stack Overflow questions.

        Build.VERSION.SDK_INT: 30
        Build.MANUFACTURER: Google
        LeakCanary version: 2.5
        App process name: com.example.memoryleakcanary
        Stats: LruCache[maxSize=3000,hits=2895,misses=62331,hitRate=4%]
        RandomAccess[bytes=3088188,reads=62331,travel=22751780355,range=17819609,size=23525415]
        Analysis duration: 3807 ms
        Heap dump file path: /storage/emulated/0/Download/leakcanary-com.example.memoryleakcanary/2020-11-11_11-43-48_771.hprof
        Heap dump timestamp: 1605113034583
        Heap dump duration: 1534 ms
        ====================================
         *
         *
         */
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        println("onDestroy() - remove reference to $tvHelloWorld")
//        app.leakedViews.remove(tvHelloWorld)
//    }

}