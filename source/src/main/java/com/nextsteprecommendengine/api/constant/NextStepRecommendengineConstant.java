package com.nextsteprecommendengine.api.constant;


public class NextStepRecommendengineConstant {
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";


    public static final Integer USER_KIND_ADMIN = 1;
    public static final Integer USER_KIND_MANAGER = 2;
    public static final Integer USER_KIND_USER = 3;
    public static final Integer USER_KIND_EMPLOYEE = 4;
    public static final Integer USER_KIND_CANDIDATE = 6;

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;

    public static final Integer NATION_KIND_PROVINCE = 1;
    public static final Integer NATION_KIND_DISTRICT = 2;
    public static final Integer NATION_KIND_COMMUNE = 3;

    public static final Integer GROUP_KIND_ADMIN = 1;
    public static final Integer GROUP_KIND_MANAGER = 2;
    public static final Integer GROUP_KIND_USER=3;
    public static final Integer GROUP_KIND_EMPLOYEE=7;
    public static final Integer GROUP_KIND_CANDIDATE=8;

    public static final Integer MAX_ATTEMPT_FORGET_PWD = 5;
    public static final int MAX_TIME_FORGET_PWD = 5 * 60 * 1000; //5 minutes
    public static final Integer MAX_ATTEMPT_LOGIN = 5;

    public static final Integer CATEGORY_KIND_NEWS = 1;
    public static final Integer CATEGORY_KIND_SKILL = 2;
    public static final Integer CATEGORY_KIND_JOB = 3;
    public static final Integer CATEGORY_KIND_LEVEL = 4;
    public static final Integer CATEGORY_KIND_EDUCATION = 5;

    public static final Integer POST_TYPE_HOME = 0;
    public static final Integer POST_TYPE_OFFICE = 1;
    public static final Integer POST_TYPE_REMOTE = 2;
    public static final Integer POST_TYPE_HYBRID = 3;

    public static final Integer POST_CONTRACT_TYPE_PART_TIME = 0;
    public static final Integer POST_CONTRACT_TYPE_FULL_TIME = 1;
    public static final Integer POST_CONTRACT_TYPE_COLLAB = 2;

    public static final Integer ACCOUNT_PLATFORM_GOOGLE = 1;

    public static final String PROCESS_EMBEDDING = "PROCESS_EMBEDDING";

    private NextStepRecommendengineConstant(){
        throw new IllegalStateException("Utility class");
    }
}
