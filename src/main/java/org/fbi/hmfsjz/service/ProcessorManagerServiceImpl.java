package org.fbi.hmfsjz.service;

import org.fbi.linking.processor.Processor;
import org.fbi.linking.processor.ProcessorManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: zhangxiaobo
 */
public class ProcessorManagerServiceImpl implements ProcessorManagerService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    public Processor getProcessor(String txnCode) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        String[] names = this.getClass().getPackage().getName().split("\\.");
        String className = names[0] + "." + names[1] + "." + names[2] + ".online.processor.T" + txnCode + "Processor";
        logger.info(className);
        Class clazz = Class.forName(className);
        Processor processor = (Processor) clazz.newInstance();
        if (processor == null) {
            throw new RuntimeException("������Processor,���׺ţ�" + txnCode);
        }
        return processor;
    }
}
