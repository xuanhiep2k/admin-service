package com.example.adminservice.utils;

import org.springframework.stereotype.Component;

@Component
public class Constants {
    public static final class CACHE {
        public static final String FUNCTION_OF_ROLE = "role.functions";
        public static final String MENU_OF_ROLE = "role.menu";
        public static final String ORGANIZATION = "organization";
        public static final String USER_DETAIL = "user.detail";
        public static final String AGGREGATION = "aggregation";
        public static final String FILTER_CONFIG = "filter.config";
        public static final String APDOMAIN = "apdomain";
        public static final String APDOMAIN_MAP = "apdomain_map";
    }

    public static class SECURITY {
        public static final String SECRET = "jkbdsfhgu834y438y";
        public static final long EXPIRATION_DAY = 1;
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String HEADER_STRING = "Authorization";
    }

    public static final class STATUS {
        public static final String ACTIVE = "ACTIVE";
        public static final String LOCKED = "LOCKED";
    }

    public static final class ROLE {
        public static final String ADMIN = "ADMIN";
    }

    public static final class RESPONSE_CODE {
        public static final String OK = "200";
        public static final String CREATED = "201";
        public static final String NO_CONTENT = "204";
        public static final String SUCCESS = "00";
        public static final String UNSUCCESS_AUTH = "203";
        public static final String INTERNAL_SERVER_ERROR = "500";
        public static final String BAD_REQUEST = "502";
        public static final String UNAUTHORIZED = "401";
        public static final String FORBIDDEN = "403";
        public static final String ERROR = "400";
        public static final String TOO_MANY_REQUESTS = "429";
    }

    public static final class ACTION {
        public static final String CREATE = "CREATE";
        public static final String UPDATE = "UPDATE";
        public static final String DELETE = "DELETE";
        public static final String LOCK = "LOCK";
        public static final String UNLOCK = "UNLOCK";
        public static final String SEARCH = "SEARCH";
    }

    public static final class TITLE_LOG {
        public static String USER = "Người dùng";
        public static String ROLE = "Nhóm quyền";
        public static String DEPARTMENT = "Phòng/ban";
        public static String PARTNER = "Đối tác";
        public static String FUNCTION = "Chức năng";
        public static String APP = "Ứng dụng";
        public static String FILTER = "Bộ lọc";
        public static String LOGIN = "Đăng nhập";
    }

    public static final class TYPE_FUNCTION {
        public static String FUNCTION = "FUNCTION";
        public static String MODULE = "MODULE";
    }
}
