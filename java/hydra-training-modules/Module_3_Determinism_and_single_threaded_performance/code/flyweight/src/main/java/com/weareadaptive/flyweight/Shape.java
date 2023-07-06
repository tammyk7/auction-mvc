package com.weareadaptive.flyweight;

import java.io.Closeable;

// Flyweight interface
interface Shape extends Closeable
{
    void draw();
}
