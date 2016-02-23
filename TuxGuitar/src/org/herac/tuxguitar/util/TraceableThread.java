package org.herac.tuxguitar.util;

import java.util.Arrays;
import java.io.PrintStream;

public class TraceableThread extends Thread {

    private StackTraceElement[][] stackTraces;

    @Override
    public void start() {
        Thread currentThread = Thread.currentThread();
        StackTraceElement stack[] = currentThread.getStackTrace();
        if (currentThread instanceof TraceableThread) {
            StackTraceElement stacks[][] = ((TraceableThread)currentThread).getStackTraces();
            stackTraces = new StackTraceElement[stacks.length + 1][];
            System.arraycopy(stacks, 0, stackTraces, 1, stacks.length);
        } else {
            stackTraces = new StackTraceElement[1][];
        }
        stackTraces[0] = cutStackHead(stack, 2);
        super.start();
    }

    public StackTraceElement[][] getStackTraces() {
        return stackTraces;
    }

    public static StackTraceElement[] cutStackHead(StackTraceElement source[], int head) {
        int length = Math.max(source.length - head, 0);
        StackTraceElement result[] = new StackTraceElement[length];
        for (int i = 0; i < length; i++) {
            result[i] = source[i + head];
        }
        return result;
    }

    public void printStackTraces(PrintStream stream) {
        for (StackTraceElement element : cutStackHead(getStackTrace(), 2)) {
            stream.println(element.toString());
        }
        int indent = 1;
        for (StackTraceElement[] stack : getStackTraces()) {
            for (int i = 0; i < indent; i++) {
                stream.print(' ');
            }
            stream.println("invoked from: ");
            for (StackTraceElement element : stack) {
                for (int i = 0; i < indent; i++) {
                    stream.print(' ');
                }
                stream.println(element.toString());
            }
            indent++;
        }
        stream.println();
    }

}
