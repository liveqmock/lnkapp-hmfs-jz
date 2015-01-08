package org.fbi.hmfsjz.internal;

import org.fbi.linking.processor.ProcessorManagerService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

public class AppActivator implements BundleActivator {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ServerService serverService;
    private static BundleContext context;

    public static BundleContext getBundleContext() {
        return context;
    }

    public void start(BundleContext context) {
        AppActivator.context = context;

        // ��Ϊ����˿���
        this.serverService = new ServerService(getBundleContext());
        this.serverService.start();

        // ������ʱ��Ϣ 20141230 ��ʱû�м�Ϣ����
        // new Thread(new InterestTimer()).start();


        ProcessorFactory factory = new ProcessorFactory();
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        properties.put("APPID", "HMFSJZ");
        context.registerService(ProcessorManagerService.class.getName(), factory, properties);

        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - Starting the HMFS-JZ app bundle....");
    }

    public void stop(BundleContext context) throws Exception {
        AppActivator.context = null;
        this.serverService.stop();
        logger.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - Stopping the HMFS-JZ app bundle...");
    }

}
