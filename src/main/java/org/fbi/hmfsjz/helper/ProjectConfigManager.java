package org.fbi.hmfsjz.helper;

import org.fbi.hmfsjz.internal.AppActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Properties;

/**
 * ��Ŀ�����ļ������������޸ĺ��������Ч.
 * User: zhanrui
 */
public class ProjectConfigManager {
    public static final Logger logger = LoggerFactory.getLogger(ProjectConfigManager.class);

    private static final String PROP_FILE_NAME = "prjcfg.properties";
    private File propFile = null;
    private long fileLastModifiedTime = 0;
    private Properties props = null;
    private static ProjectConfigManager manager = new ProjectConfigManager();

    private ProjectConfigManager() {
        //URL url = ProjectConfigManager.class.getClassLoader().getResource(PROP_FILE_NAME);
        BundleContext bundleContext = AppActivator.getBundleContext();

        URL url = bundleContext.getBundle().getEntry(PROP_FILE_NAME);

        props = new Properties();
        try {
            props.load(url.openConnection().getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


/*
        URL url = getClass().getResource(PROP_FILE_NAME);
        if (url == null) {
            logger.error("�����ļ�������!");
            throw new RuntimeException("�����ļ�������!");
        }
        propFile = new File(url.getFile());
        fileLastModifiedTime = propFile.lastModified();
        props = new Properties();
        try {
            props.load(new FileInputStream(propFile));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
*/
    }

    public static ProjectConfigManager getInstance() {
        return manager;
    }

    final public String getProperty(String name) {
/*
        long newTime = propFile.lastModified();

        if (newTime == 0) {
            if (fileLastModifiedTime == 0) {
                System.err.println(PROP_FILE_NAME + " �ļ�������.");
            } else {
                System.err.println(PROP_FILE_NAME + " �ļ��ѱ�ɾ��!");
            }
            return null;
        } else if (newTime > fileLastModifiedTime) {
            props.clear();
            try {
                props.load(new FileInputStream(propFile));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        fileLastModifiedTime = newTime;
*/
        return props.getProperty(name);
    }
}