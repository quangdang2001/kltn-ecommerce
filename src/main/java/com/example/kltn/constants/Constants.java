package com.example.kltn.constants;

public class Constants {
    public static class USER {
        public static class GENDER {
            public static final String MALE = "male";
            public static final String FEMALE = "female";
            public static final String OTHER = "other";
        }
        public static class ROLE {
            public static final String ADMIN = "admin";
            public static final String STAFF = "staff";
            public static final String CUSTOMER = "customer";
        }
        public static class STATUS {
            public static final String ACTIVE = "active";
            public static final String INACTIVE = "inactive";
            public static final String LOCKED = "locked";
        }
    }

    public static class ORDER {
        public static class STATUS {
            public static final String PENDING = "pending";
            public static final String CONFIRMED = "confirmed";
            public static final String SHIPPING = "shipping";
            public static final String COMPLETED = "completed";
            public static final String CANCELLED = "cancelled";
        }
        public static class PAYMENT_METHOD {
            public static final String CASH = "cash";
            public static final String COD = "cod";
            public static final String VNPAY = "vnpay";
            public static final String MOMO = "momo";
            public static final String PAYPAL = "paypal";
            public static final String ZALO_PAY = "zalopay";
        }
        public static class PAYMENT_STATUS {
            public static final String PENDING = "pending";
            public static final String PAID = "paid";
            public static final String CANCELLED = "cancelled";
        }
    }
}
