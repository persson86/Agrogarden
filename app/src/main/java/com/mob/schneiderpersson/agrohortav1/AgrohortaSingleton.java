package com.mob.schneiderpersson.agrohortav1;

public final class AgrohortaSingleton {

    public String salvoMemoria;

    private AgrohortaSingleton() {
    }

    private static volatile AgrohortaSingleton instance = null;

    public static AgrohortaSingleton getInstance() {
        if (instance == null) {
            synchronized (AgrohortaSingleton.class) {
                instance = new AgrohortaSingleton();
            }
        }
        return instance;
    }

    //region Pattern
/*    private static class LazyHolder {
        private static final AgrohortaSingleton INSTANCE = new AgrohortaSingleton();
    }

    public static AgrohortaSingleton getInstance() {
        return LazyHolder.INSTANCE;
    }*/
    //endregion

    public void saveTest(String teste) {

        salvoMemoria = teste;

    }

    public String getMemoria() {
        return salvoMemoria;
    }
}
