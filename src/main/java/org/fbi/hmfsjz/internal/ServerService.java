package org.fbi.hmfsjz.internal;

import org.fbi.hmfsjz.gateway.server.NettyServer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public final class ServerService implements Runnable {
    private static final String PID = "linking.server.hmfsjz.nettyservice";

    private final BundleContext context;
    private boolean running;
    private Thread thread;
    private ServiceRegistration configServiceReg;
    private NettyServer server;

    public ServerService(BundleContext context) {
        this.context = context;
    }

    public void start() {
        this.thread = new Thread(this, PID);
        this.thread.start();
    }

    public void stop() throws Exception {
        if (this.configServiceReg != null) {
            this.configServiceReg.unregister();
        }

        this.running = false;
        this.thread.interrupt();

        try {
            this.thread.join(3000);
        } catch (InterruptedException e) {
            //
        }
    }

    @Override
    public void run() {
        this.running = true;
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

        while (this.running) {
            startNetty();

            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    //
                }
            }
            stopNetty();
        }
    }


    private void startNetty() {
        try {
            this.server = new NettyServer();
            this.server.run();
        } catch (Exception e) {
            //TODO SystemLogger.error
        }
    }

    private void stopNetty() {
        if (this.server != null) {
            try {
                this.server.stop();
                this.server = null;

                System.out.println("Netty server stopped.............");

            } catch (Exception e) {
                //TODO SystemLogger.error
            }
        }
    }

}
