// IMusicPlayer.aidl
package com.prabhunathan.cs478.p5.cs478_proj5_1_playerclient;

// Declare any non-default types here with import statements

interface IMusicPlayer {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
        void play(int songID);
        void pause(int songID);
        void resume(int songID);
        void stop(int songID);
        void getTable();
}
