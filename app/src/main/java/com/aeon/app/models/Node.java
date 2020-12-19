package com.aeon.app.models;

/*
Copyright 2020 ivoryguru

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

public class Node {
    private static final String TAG = "Node";
    public String hostAddress;
    public String hostPort;
    public long height;
    public long target;
    public long version;
    public static ArrayList<Node> nodes;

    static {
        nodes = new ArrayList<>();
        nodes.add(new Node("144.76.113.157"));
        nodes.add(new Node("142.93.155.14"));
        nodes.add(new Node("95.111.247.164"));
        nodes.add(new Node("95.216.145.71"));
        nodes.add(new Node("3.14.4.25"));
        //nodes.add(new Node("144.76.113.157"));
        //nodes.add(new Node("194.116.45.216"));
        //nodes.add(new Node("5.45.108.241","32405"));
    }

    public Node(String hostAddress){
        this(hostAddress,"11181");
    }
    public Node(String hostAddress, String port) {
        Log.v(TAG, "Node");
        this.version=-1;
        this.hostAddress = hostAddress;
        this.hostPort = port;
    }

    public static Node pickRandom(){
        Node newNode  = nodes.get(new Random().nextInt(nodes.size()));
        return new Node(newNode.hostAddress, newNode.hostPort);
    }
    public static Node pickRandom(String ip){
        Node newNode  = nodes.get(new Random().nextInt(nodes.size()));
        while(newNode.hostAddress.equals(ip)){
            newNode  = nodes.get(new Random().nextInt(nodes.size()));
        }
        return new Node(newNode.hostAddress, newNode.hostPort);
    }
}
