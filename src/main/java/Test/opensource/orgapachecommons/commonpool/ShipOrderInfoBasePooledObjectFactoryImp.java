package Test.opensource.orgapachecommons.commonpool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ShipOrderInfoBasePooledObjectFactoryImp extends BasePooledObjectFactory<ShipOrderInfo> {
    @Override
    public ShipOrderInfo create() throws Exception {
        return new ShipOrderInfo();
    }

    @Override
    public PooledObject<ShipOrderInfo> wrap(ShipOrderInfo o) {
        return new DefaultPooledObject<ShipOrderInfo>(o);
    }
}
