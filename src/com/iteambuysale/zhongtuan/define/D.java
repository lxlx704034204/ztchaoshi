package com.iteambuysale.zhongtuan.define;

/**
 * @author Administrator
 *
 */
public class D {
	/* version */
	//	public static final String VERSION_STATUS = "debug";// debug or release;
	public static final String VERSION = "1.2.2";
	public static final String VERSION_CODE = "23";

	public static final String VERSION_NAME = "阿尔卑斯山";

	public static final int DB_VERSION = 153;
    public static final int LOAD_EVERY_PAGE_NUMBER=10;//分页加载每页加载的个数；
	//定位开关
	public static final Boolean LOCATION_SWITCH = true;
	
	/* local File Path */
	public static final String IMAGE_CACHE = "/TeamBuy/.img_cache/";
	public static final String FILE_CACHE = "/TeamBuy/.file_cache/";
	
	/* download */
	public static final String DOWN_YIN_LIAN = "http://mobile.unionpay.com/getclient?platform=android&type=securepayplugin";
   /* api */

	public static final String BASIC_URL = "http://app.teambuy.com.cn/apnc";
	public static final String TM_ADVER_PIC=BASIC_URL+"/m/sys/a/getappadv";
	public static final String API_USER_REGISTER = BASIC_URL + "/m/user/a/register";
	public static final String API_USER_LOGIN = BASIC_URL + "/m/user/a/login";
	public static final String API_USER_SENDYZM = BASIC_URL + "/m/user/a/sendyzm";
	public static final String API_USER_CHECKUSERNAME = BASIC_URL + "/m/user/a/checkusername";
	public static final String API_USER_CHPWD = BASIC_URL + "/m/my/a/chpwd";
	public static final String API_USER_EDITUSER = BASIC_URL + "/m/my/a/edituser";
	public static final String API_USER_LOGINBYTOKE = BASIC_URL + "/m/user/a/loginbytoke";
	public static final String API_SHOP_GETSHOP = BASIC_URL + "/m/shop/a/getshop";
	public static final String API_SHOP_GETASHOP = BASIC_URL + "/m/shop/a/getashop";
	public static final String API_CPMX_GETCPMXBYSHOP = BASIC_URL + "/m/cpmx/a/getcpmxbyshop";
	public static final String API_CPMX_GETACPMX = BASIC_URL + "/m/cpmx/a/getacpmx";
	public static final String API_CPMX_GETALLCPMX = BASIC_URL + "/m/cpmx/a/getallcpmx";
	public static final String API_CPMX_GETTEMAI = BASIC_URL + "/m/cpmx/a/gettemai";
	public static final String API_CPMX_GETHUODONG = BASIC_URL + "/m/cpmx/a/gethuodong";
	public static final String API_CPMX_GETTBH = BASIC_URL + "/m/cpmx/a/gettuanbohui";
	public static final String API_CPMX_GETONEHD = BASIC_URL + "/m/cpmx/a/getonehd";
	public static final String API_CPORD_GETADDRESS = BASIC_URL + "/m/cpord/a/getaddress";
	public static final String API_CPORD_NEWADDRESS = BASIC_URL + "/m/cpord/a/newaddress";
	public static final String API_CPORD_CREATEORDER = BASIC_URL + "/m/cpord/a/createorder";
	public static final String API_CPORD_CREATEPAY = BASIC_URL + "/m/tmord/a/createpay";
	public static final String API_CPORD_ORDRECCOM = BASIC_URL + "/m/cpord/a/ordreccom";
	public static final String API_CPORD_ORDRECGOODS = BASIC_URL + "/m/cpord/a/ordrecgoods";
	public static final String API_SYS_GETNEWVER = BASIC_URL + "/m/sys/a/getnewver";
	public static final String API_MY_GETTGORDER = BASIC_URL + "/m/my/a/getmyorder";
	public static final String API_MY_FEEDBACK = BASIC_URL + "/m/my/a/feedback";
	public static final String API_MY_TMORDERMX = BASIC_URL + "/m/my/a/getmytmordmx";//查看物流的订单明细
	public static final String API_CPORD_UPDATEADDRESSS = BASIC_URL + "/m/cpord/a/editaddress";
	public static final String API_ACTIVITY_SIGN_UP = BASIC_URL + "/m/cpord/a/hdnewbm";
	public static final String API_TBH_SIGN_UP = BASIC_URL + "/m/cpord/a/tuanbohuibm";
	public static final String API_CPORD_ORDER_CANCEL = BASIC_URL + "/m/tmord/a/ordcancel";
	public static final String API_CPORD_ORDER_REFUND = BASIC_URL + "/m/cpord/a/ordrefund";
	public static final String API_SHOP_GETALL = BASIC_URL + "/m/shop/a/getshopdeal";
	public static final String API_PRODUCT_TG_ORDER = BASIC_URL +"/m/cpord/a/createtuanorder";
	public static final String API_ME_USER_MSG = BASIC_URL +"/m/my/a/getmyinfo";
	public static final String API_ME_ZTQ = BASIC_URL +"/m/my/a/getmyquan";
	public static final String API_BUSINESS_CATEGORY = BASIC_URL +"/m/cpmx/a/getcomplb";
	public static final String API_PRODUCT_CATEGORY = BASIC_URL +"/m/cpmx/a/getcplb";
	public static final String API_DELETE_ORDERS = BASIC_URL +"/m/cpord/a/ordscancel";
	public static final String API_PRODUCT_WEBVIEW ="http://app.teambuy.com.cn/webc/m/model/id/";
	public static final String API_SHOP_WEBVIEW = "http://app.teambuy.com.cn/webc/m/shop/id/";
	//特卖图文详情
	public static final String API_TEMAI_WEBVIEW = "http://app.teambuy.com.cn/webc/m/temai/id/";
	//temai
	public static final String API_SPECIAL_SELL =BASIC_URL +"/m/temai/a/gettemai";//http://app.teambuy.com.cn/apc/m/temai/a/gettemai
    public static final String API_SPECIAL_GETTMLB=BASIC_URL+"/m/temai/a/gettmlb";
    public static final String API_SPECIAL_GETWULIU=BASIC_URL+"/m/temai/a/gettmlogi";//获取物流信息
    //获取一个特卖商品的信息
    public static final String API_SPECIAL_GETATEMAI=BASIC_URL+"/m/temai/a/getatemai";
    public static final String API_SPECIAL_CREATEORDER=BASIC_URL+"/m/tmord/a/createtmorder";
    public static final String API_SPECIAL_CREATEPAY=BASIC_URL+"/m/tmord/a/createpaybych";
    //确认收货
    public static final String API_SPECIAL_ORDER_ENSURE_GOODS=BASIC_URL+"/m/tmord/a/ordrecgoods";
    //获取特卖商品的评价
    public static final String API_SPECIAL_ORDER_GETTMRECM=BASIC_URL+"/m/temai/a/gettmrecm";
    //提交特卖订单评价
    public static final String API_SPECIAL_ORDER_ORDERCCOM=BASIC_URL+"/m/tmord/a/ordreccom";
    //特卖退款
    public static final String API_SPECIAL_ORDREFUND=BASIC_URL+"/m/tmord/a/ordrefund";
    //特卖产品允许购买的数量
    public static final String API_SPECIAL_BUY_NUM=BASIC_URL+"/m/tmord/a/allowbuy";
    //特卖取消退款
    public static final String API_SPECIAL_CANCEL_ORDREFUND=BASIC_URL+"/m/tmord/a/ordrefdel";
    //批量取消订单
    public static final String API_SPECIAL_ORDRSCANCEL=BASIC_URL+"/m/tmord/a/ordscancel";
    //删除一个地址
    public static final String API_SPECIAL_DELUADDR=BASIC_URL+"/m/cpord/a/deluaddr";
    //获取某一店铺的商品
    //TODO
    public static final String API_SPECIAL_GETSHOP=BASIC_URL+"/m/temai/a/gettmshopbyid";
    public static final String API_SPECIAL_GET_ALL_SHOP=BASIC_URL+"/m/temai/a/gettmshop";
    //获取某一店铺的商品信息
    public static final String API_SPECIAL_GET_PRODUCT_IMGS=BASIC_URL+"/m/temai/a/gettminfo";
    //获取产品的尺码
    public static final String API_SPECIAL_GET_PRODUCT_CHIMA=BASIC_URL+"/m/temai/a/gettmcima";
    public static final String API_SPECIAL_ADD_TO_CART=BASIC_URL+"/m/tmord/a/addcart";//把商品加入购物车
    public static final String API_SPECIAL_GET_CART=BASIC_URL+"/m/tmord/a/getcart";//获取购物车
    public static final String API_SPECIAL_EDIT_CART=BASIC_URL+"/m/tmord/a/editcart";//修改购物车订单
    public static final String API_SPECIAL_CART_ORDER_POST=BASIC_URL+"/m/tmord/a/cartorder";//修改购物车订单
    public static final String API_SPECIAL_CART_DELETE_ORDER=BASIC_URL+"/m/tmord/a/delcart";//删除订单
    
    public static final String ORDER_TUANBI_MONEY=BASIC_URL+"/m/tmord/a/ordtbmoney";//下订单使用团币接口
	public static final String API_GETQBYORDNO=BASIC_URL+"/m/my/a/getquanbyord";
	public static final String API_GET_EVALUATION=BASIC_URL+"/m/cpmx/a/getcprecm";
	
	public static final String API_MY_SETAVATAR = BASIC_URL + "/m/my/a/setavatar";
	public static final String API_MY_GETTMORDER = BASIC_URL + "/m/my/a/getmytmord";
	public static final String API_MY_GETTMORDER2 = BASIC_URL + "/m/my/a/getmytmordnocpmx";
	public static final String API_CHECK_COLLECT = BASIC_URL + "/m/my/a/isfav";
	public static final String GET_TEMAI_QUAN = BASIC_URL + "/m/my/a/gettmquan";
	/*
	 * 支付方式
	 */
	public static final String PAY_BY_ALI="alipay";
	public static final String PAY_BY_WECHAT="wx";
	public static final String PAY_BY_YINLIAN="upmp";
	public static final String API_ADD_COLLECT = BASIC_URL + "/m/my/a/addfav";
	public static final String API_CANCEL_COLLECT = BASIC_URL + "/m/my/a/subfav";
	public static final String API_GETALL_COLLECT = BASIC_URL + "/m/my/a/getmyfav";
	public static final String API_GET_MY_NEAR_EVALUTE=BASIC_URL+"/m/my/a/getmyrecm";//	获取我的周边的评论
	public static final String API_GET_MY_TEMAI_EVALUTE=BASIC_URL+"/m/my/a/gettmrecm";//获取我的特卖的评论
	public static final String API_GET_FEEDBACK=BASIC_URL+"/m/my/a/getfblist";
	
	public static final String VERY_YZM=BASIC_URL+"/m/user/a/veryzm";
	
	/* Network */
	public static final String NETWORK_STATUS_CODE = "STATUS";
	public static final String NETWORK_CONTENT = "CONTENT";
//	public static final String NETWORK_SESSIONID = "SESSIONID";
	public static final String FLAG_SUCCESS = "1";
	public static final String FLAG_FAILED = "0";
	public static final String FLAG_SESSION_TIMEOUT = "-1";
	public static final String FLAG_TOKEN_TIMEOUT = "-2";
	public static final String KEY_RET_RET = "ret";
	public static final String KEY_RET_DATA = "data";
	public static final String KEY_RET_ERRMSG = "errmsg";
	public static final String KEY_RET_SESSID = "sessid";
	
	/* databases */
	public static final String EQU = "=";
	public static final String LIKE = "LIKE";
	public static final String AND = "AND";
	public static final String OR = "OR";
	public static final int COLUMN_NAME = 1;
	public static final int ARG_NAME = 2;
	
	/* network args */
	// login
	public static final String ARG_LOGIN_PHONE = "username"; // 用户名（手机号）
	public static final String ARG_LOGIN_PASW = "password"; // 密码
	public static final String ARG_LOGIN_BY_TOKEN = "acctoken";
	// register
	public static final String ARG_REGISTER_PHONE = "username";
	public static final String ARG_REGISTER_PASW = "password";
	public static final String ARG_REGISTER_MOBY_YZM = "mobyzm";
	public static final String ARG_REGISTER_NICKNAME = "nickname";
	public static final String ARG_REGISTER_BIRTHDAY = "birthday";
	public static final String ARG_REGISTER_SEX = "sex";
	public static final String ARG_REGISTER_SESSIONID = "sessid";
	public static final String ARG_REGISTER_MOBILE = "mobile";
	// shop list
	public static final String ARG_GETSHOPS_CITYID = "cityid";
	public static final String ARG_GETSHOPS_LNGO = "lngo";
	public static final String ARG_GETSHOPS_LATO = "lato";
	public static final String ARG_GETSHOPS_PAGE = "page";
	// store
	public static final String ARG_STORE_ID = "shopid"; // 商铺id

	// product
	public static final String ARG_PRODUCT_PAGE = "page"; // 页码
	public static final String ARG_PRODUCT_MODID = "modid"; // 商品id
	public static final String ARG_PRODUCT_TMID = "tmid"; // 商品id
	public static final String TEMAI_CPDL="cpdl";//cpdl/大类ID
	

	// teamBuy product
	public static final String ARG_TEAMBUY_PAGE = "page"; // 页码
	public static final String ARG_TEAMBUY_PX = "px"; // 排序
	public static final String ARG_TEAMBUY_CITYID = "cityid"; // 城市id

	// add address
	public static final String ARG_CHOOSE_TRUENAME = "truename";
	public static final String ARG_CHOOSE_TEL = "tel";
	public static final String ARG_CHOOSE_ADDRESS = "address";
	public static final String ARG_CHOOSE_PROVINCE = "province";
	public static final String ARG_CHOOSE_CITY = "city";
	public static final String ARG_CHOOSE_ZIPCODE = "zipcode";
	public static final String ARG_CHOOSE_ISDEF = "isdef";
	public static final String ARG_UPDATE_UAID = "uaid";

	// create order
	public static final String ARG_ORDER_UAID = "addrid";
	public static final String ARG_ORDER_PAYM = "paym";
	public static final String ARG_ORDER_SENDM = "sendm";
	public static final String ARG_ORDER_FAPIAO = "fapiao";
	public static final String ARG_ORDER_LNGO = "lngo";
	public static final String ARG_ORDER_LATO = "lato";
	public static final String ARG_ORDER_SHOP = "shop";
	public static final String ARG_ORDER_CPMX = "cpmx";
	public static final String ARG_ORDER_CREDENTIAL = "credential";

	// Sale & Activities
	public static final String ARG_TM_PAGE = "page";	// 页码
	public static final String ARG_AC_AC = "ac";		// 本地区号
	public static final String ARG_AC_TGNO = "id";     // 活动编号
	public static final String ARG_AC_TRUENAME = "truename";
	public static final String ARG_AC_TEL = "tel";
	public static final String ARG_AC_ADDRESS = "address";
	public static final String ARG_AC_REAPP = "reapp";
	public static final String ARG_AC_MEMO = "memo";
	// edit password
	public static final String ARG_EDITPSW_USERNAME = "username";
	public static final String ARG_EDITPSW_PSW = "password";
	public static final String ARG_EDITPSW_MOBYZM = "mobyzm";
	
	// user Edit
	public static final String ARG_EDITUSER_NICKNAME = "nickname";
	public static final String ARG_EDITUSER_BIRTHDAY = "birthday";
	public static final String ARG_EDITUSER_SEX = "sex";
	public static final String ARG_EDITUSER_EMAIL = "email";
	public static final String ARG_EDITUSER_SIGNATE = "signate";
	
	// feedback 
	public static final String ARG_FEEDBACK = "feedback";
	
	/* db */
	public static final String DB_NAME = "ZHONG_TUAN";
	public static final String DB_NAME_PROVINCE="china_province_city_zone.db";
	public static final String DB_TABLE_USER = "USER_LIST";
	public static final String DB_TABLE_PROVINCE = "T_Province";
	public static final String DB_TABLE_CITY = "T_City";
	public static final String DB_TABLE_ADDRESS_LIST = "ADDRESS_LIST";
	public static final String DB_TABLE_STORE_LIST = "STORE_LIST";
	public static final String DB_TABLE_PRODUCT_LIST = "PRODUCT_LIST";
	public static final String DB_TABLE_TEMAI_LIST = "TEMAI_LIST";
	//public static final String DB_TABLE_TEMAI_LISTVERSON = "TEMAI_LISTVERSON";
	public static final String DB_TABLE_EVENT_LIST = "ACTIVITIES_LIST";
	public static final String DB_TABLE_TBH_LIST = "TUANBOHUI_LIST";
	public static final String DB_TABLE_TEMAI_LIST_VERSONT = "TEMAI_LIST_VERSON";
	public static final String DB_TABLE_TEMAI_CATAGORY = "TEMAI_CATAGORY";

	// PROVINCE COLUMN NAME
	public static final String DB_PROVINCE_COL_ID = "_id";
	public static final String DB_PROVINCE_COL_NAME = "_province_name";
	// CITY COLUMN NAME
	public static final String DB_CITY_COL_ID = "_id";
	public static final String DB_CITY_COL_CODE = "_id";
	public static final String DB_CITY_COL_NAME = "CityName";
	public static final String DB_CITY_COL_CITYCODE = "CityCode";
	public static final String DB_CITY_COL_PROVINCE_ID = "ProID";
	// ADDRESS LIST COLUMN NAME
	public static final String DB_ADDRESS_LIST_COL_UAID = "_id"; // uaid
	public static final String DB_ADDRESS_LIST_COL_USERNAME = "_username";
	public static final String DB_ADDRESS_LIST_COL_PROVINCE = "_province";
	public static final String DB_ADDRESS_LIST_COL_CITY = "_city";
	public static final String DB_ADDRESS_LIST_COL_ADDRESS = "_address";
	public static final String DB_ADDRESS_LIST_COL_TRUENAME = "_truename";
	public static final String DB_ADDRESS_LIST_COL_TEL = "_tel";
	public static final String DB_ADDRESS_LIST_COL_ZIPCODE = "_zipcode";
	public static final String DB_ADDRESS_LIST_COL_ISDEF = "_isdef";

	// STORE LIST
	public static final String DB_STORE_LIST_SID = "_id";
	public static final String DB_STORE_LIST_SHOPNAME = "_shopname";
	public static final String DB_STORE_LIST_PICURL = "_picurl";
	public static final String DB_STORE_LIST_ADDRESS = "_address";
	public static final String DB_STORE_LIST_TEL = "_tel";
	public static final String DB_STORE_LIST_LNGO = "_lngo";
	public static final String DB_STORE_LIST_LATO = "_lato";
	public static final String DB_STORE_LIST_PERFEE = "_prefee";
	public static final String DB_STORE_LIST_ZFEN = "zfen";
	public static final String DB_STORE_LIST_DISTANCE = "_distance";

	// PRODUCT LIST
	public static final String DB_PRODUCT_MID = "_id";
	public static final String DB_PRODUCT_CPMC = "_cpmc";
	public static final String DB_PRODUCT_CPGG = "_cpgg";
	public static final String DB_PRODUCT_PICURL = "_picurl";
	public static final String DB_PRODUCT_CPDL = "_cpdl";
	public static final String DB_PRODUCT_CPXL = "_cpxl";
	public static final String DB_PRODUCT_URLQRCODE = "_urlqrcode";
	public static final String DB_PRODUCT_DJ0 = "_dj0";
	public static final String DB_PRODUCT_DJ1 = "_dj1";
	public static final String DB_PRODUCT_DJ2 = "_dj2";
	public static final String DB_PRODUCT_KCSL = "_kcsl";
	public static final String DB_PRODUCT_SELLS = "_sells";
	public static final String DB_PRODUCT_JLDW = "_jldw";
	public static final String DB_PRODUCT_BESTS = "_bests";
	public static final String DB_PRODUCT_COLLECTS = "_collects";
	public static final String DB_PRODUCT_SHOPID = "_shopid";
	public static final String DB_PRODUCT_CONTENT = "_content";

	// TEMAI LIST
	public static final String DB_TEMAI_ID = "_id";
	public static final String DB_TEMAI_TITLE = "_title";
	public static final String DB_TEMAI_TMWORD = "_tmword";
	public static final String DB_TEMAI_TMLB = "_tmlb";
	public static final String DB_TEMAI_TMURL = "_tmurl";
	public static final String DB_TEMAI_MID = "_mid";
	public static final String DB_TEMAI_TMDJ = "_tmdj";
	public static final String DB_TEMAI_SDATE = "_sdate";
	public static final String DB_TEMAI_EDATE = "_edate";
	public static final String DB_TEMAI_PICURL = "_picurl";
	public static final String DB_TEMAI_ZKL = "_zkl";
	public static final String DB_TEMAI_YUANDJ = "_yuandj";
	//temai order
	public static final String ORDER_BY_DATE="_edate desc";
	public static final String ORDER_BY_SELL="_sells desc";
	public static final String ORDER_BY_PRICE_DESC="_tmdj desc";
	public static final String ORDER_BY_PRICE_ASC="_tmdj asc";

	// EVENTS
	public static final String DB_EVENT_EID = "_eid";
	public static final String DB_EVENT_EXNAME = "_exname";
	public static final String DB_EVENT_EXDATE = "_exdate";
	public static final String DB_EVENT_WWW = "_www";
	public static final String DB_EVENT_CZY = "_czy";
	public static final String DB_EVENT_XH = "_xh";
	public static final String DB_EVENT_DETAIL = "_detail";
	public static final String DB_EVENT_ZT = "_zt";
	public static final String DB_EVENT_EDATE = "_edate";
	public static final String DB_EVENT_JOINLC = "_joinlc";
	public static final String DB_EVENT_WEIQUAN = "_weiquan";
	public static final String DB_EVENT_PICLARGE = "_piclarge";
	public static final String DB_EVENT_PICLARGEURL = "_piclargeurl";
	public static final String DB_EVENT_TUIGUANG = "_tuiguang";
	public static final String DB_EVENT_PICHFBZ = "_pichfbz";
	public static final String DB_EVENT_PICHF = "_pichf";
	public static final String DB_EVENT_REAPP = "_reapp";
	public static final String DB_EVENT_AREACODE = "_areacode";
	public static final String DB_EVENT_MOBMEMO = "_mobmemo";
	public static final String DB_EVENT_ID = "_id";
	public static final String DB_EVENT_TGNO = "_tgno";
	public static final String DB_EVENT_TGNAME = "_tgname";
	public static final String DB_EVENT_TITLE = "_title";
	public static final String DB_EVENT_ADDRESS = "_address";
	public static final String DB_EVENT_URL = "_url";
	public static final String DB_EVENT_TDATE = "_tdate";
	public static final String DB_EVENT_TTIME = "_ttime";
	public static final String DB_EVENT_BMTEL = "_bmtel";
	public static final String DB_EVENT_BMQQ = "_bmqq";
	public static final String DB_EVENT_PICURL = "_picurl";


	/* model */
	public static final int MALE = 0;
	public static final int FEMALE = 1;
	public static final int UNKNOW = 2;

	/* common */
	public static final String PREFERENCE_NAME = "zhongtuan_preference";

	/* for test & debug */
	public static final String NETWORK_DEBUG = "NET_DEBUG";
	public static final String DB_DEBUG = "DB_DEBUG";
	public static final String DEBUG = "DEBUG";
	public static final String LOCATION_DEBUG = "LOCATION_DEBUG";

	public static final String DEFAULT_DESCRIPTION = "店铺介绍：用于测试的店铺介绍内容 ";

	/* validate */
	public static final String ERROR_CODE_PHONE_NOTMATCH = "601";
	public static final String ERROR_MSG_PHONE_NOTMATCH = "手机号码格式不正确，请输入11位手机号码";
	public static final String ERROR_CODE_PASW_NOT_MATCH = "602";
	public static final String ERROR_MSG_PASW_NOTMATCH = "密码格式不正确，请重新输入6位以上正确密码";
	public static final String ERROR_CODE_PASW_NOT_EQUAL = "603";
	public static final String ERROR_MSG_PASW_NOT_EQUAL = "两次输入的密码不一致！请重新设置密码";
	public static final String ERROR_CODE_NETWORK_DISCONNECT = "604";
	public static final String ERROR_MSG_NETWORK_DISCONNECT = "你还没有链接网络";
	public static final String ERROR_CODE_NETWORK_SESSION_TIME_OUT = "605";
	public static final String ERROR_MSG_NETWORK_SESSION_TIME_OUT = "session 已经过期";
	public static final String ERROR_CODE_NICKNAME_NULL = "606";
	public static final String ERROR_MSG_NICKNAME_NULL = "请输入昵称";
	public static final String ERROR_CODE_SEX_NOTSELECT = "607";
	public static final String ERROR_MSG_SEX_NOTSELECT = "请选择性别";
	public static final String ERROR_CODE_BIRTHDAY_NOTSELECT = "608";
	public static final String ERROR_MSG_BIRTHDAY_NOTSELECT = "请选择出生日期";
	public static final String ERROR_CODE_YZM_NULL = "609";
	public static final String ERROR_MSG_YZM_NULL = "请输入6位验证码";
	public static final String ERROR_CODE_REPEAT_NAME = "700";
	public static final String ERROR_MSG_REPEAT_NAME = "用户名已经存在，请直接登录";
	public static final String ERROR_MSG_CHOOSE_NAME_NULL = "姓名不能为空";
	public static final String ERROR_MSG_CHOOSE_ADDRESS_NULL = "请填写地址";
	public static final String ERROR_MSG_CHOOSE_ZIPCODE_NOT_MATCH = "请输入有效的邮编";

	/* Location */
	public static final int INDEX_PROVINCE = 0;
	public static final int INDEX_CITY = 1;
	public static final int INDEX_AREA = 2;
	public static final int INDEX_LNGO = 3;
	public static final int INDEX_LATO = 4;

	/* city map */
	public static final int INDEX_CITY_NAME = 2;
	public static final int INDEX_CITY_ID = 0;
	public static final int INDEX_CITY_CODE = 1;
	public static final int INDEX_CITY_PROVINCEID = 3;
	public static final int INDEX_CITY_PROVINCENAME = 4;
	public static final int INDEX_CITY_INDEX_CODE = 5;

	/* province map */
	public static final int INDEX_PRO_ID = 0;
	public static final int INDEX_PRO_AREA = 1;
	public static final int INDEX_PRO_PRO_NAME = 2;
	public static final int INDEX_PRO_PRO_TYPE = 3;

	/* bundle */
	public static final String BUNDLE_STORE = "store";
	public static final String BUNDLE_PRODUCT = "product";
	public static final String BUNDLE_STORE_ID = "store_id";
	public static final String BUNDLE_URL = "url";
	public static final String BUNDLE_TGNO = "tgno";
	
	public static final String EX_ID = "ID";

	/* request code */
	public static final int REQUEST_UAID = 0;
	public static final int RESPONSE_UAID = 1;

	/* optinos */
	public static final int OPT_PRODUCT = 0;
	public static final int OPT_LINK = 1;

	/* title bar */
	public static final int BAR_SHOW_LEFT = 0;
	public static final int BAR_SHOW_RIGHT = 1;
	public static final int BAR_SHOW_BOTH = 2;
	public static final int BAR_SHOW_NONE = 3;

	/* time */
	public static final int YMD = 0;
	public static final int YEAR = 0;
	public static final int MONTH = 1;
	public static final int DAY = 2;
	
	/* list pull down */
	public static final String NOMAL = "nomal";
	public static final String HALF_PULL = "half_down";
	public static final String FULL_PULL = "full";
	
	/* ORDER DETAIL OPTION */
	public static final String OPT_PAYMENT = "0";	//继续支付
	public static final String OPT_CONFORM = "1";	//确认收货 (已付款)
	public static final String OPT_COMMENT = "2"; 	//已收货，待评价（已消费）
	public static final String OPT_HISTORY = "3";	//评价完成
	public static final String OPT_SERVICE="4";		//商家服务     0
	public static final String OPT_REFUND="5";		//退款处理中
	public static final String OPT_REFUND_OK="6";	//同意退款     0
	public static final String OPT_SERVICE_OK="7";	//服务完成      0
	public static final String OPT_REFUND_FINISH="8";	//退款完成
	public static final String OPT_REFUNDING="9";	//退款中
	
	/* HOME OPTION */
	public static final String OPT_NEAR = "0";		//附近
	public static final String OPT_SALE = "1";		//特卖
	public static final String OPT_ACTIVITIES = "2";//活动
	public static final String OPT_ME = "3";		//我的
	
	/*评价 level*/
	public static final String ARG_OPT_COMMENT_LEVEL_GOOD = "2";
	public static final String ARG_OPT_COMMENT_LEVEL_MID = "1";
	public static final String ARG_OPT_COMMENT_LEVEL_BAD = "0";
	
	/*修改密码*/
	public static final String CHANGE_PSW_OUT = "0";
	public static final String CHANGE_PSW_IN = "1";
	
	/*image loader*/
	public static final String IMAGE_TYPE_TINY = "tiny";
	public static final String IMAGE_TYPE_ORGN = "origin";
	public static final String LIST_STATE_SCROLL = "scrolling";
	public static final String LIST_STATE_IDLE = "idle";
	
	public static final String CATE_PTAG_1 = "";
	public static final String CATE_PTAG_2 = "8";
	public static final String CATE_PTAG_3 = "9";
	public static final String CATE_PTAG_4 = "1";
	public static final String CATE_PTAG_5 = "46";
	public static final String CATE_PTAG_6 = "63";
	public static final String CATE_PTAG_7 = "";
	
	public static final String DEBUG_VERBOSE="0";
	public static final String DEBUG_DEBUG="1";
	public static final String DEBUG_INFO="2";
	public static final String DEBUG_WARN="3";
	public static final String DEBUG_ERROR="4";
	public static final String DEBUG_ASSERT="5";
	
	//腾讯云分析渠道打包
	public static final String PACKAGE_BAIDU="百度手机助手";
	public static final String PACKAGE_91="91手机助手";
	public static final String PACKAGE_ANDROID="安卓市场";
	public static final String PACKAGE_TENCENT="应用宝";
	public static final String PACKAGE_360="360手机助手";
	public static final String PACKAGE_TAOBAO="淘宝手机助手";
	public static final String PACKAGE_WANDOUJIA="豌豆荚";
	public static final String PACKAGE_XIAOMI="小米应用市场";
	public static final String PACKAGE_HUAWEI="华为应用市场";
	public static final String PACKAGE_LENOVO="联想乐商店";
	public static final String PACKAGE_ANZHI="安智市场";
	public static final String PACKAGE_JIFENG="机锋";
	public static final String PACKAGE_MEIZU="魅族";
	public static final String PACKAGE_OPPO="OPPO软件商店";
	public static final String PACKAGE_NDUO="N多市场";
	public static final String PACKAGE_MUMAYI="木蚂蚁";
	public static final String PACKAGE_HONGMA="红码";
	public static final String PACKAGE_TIANYI="天翼空间";
	public static final String PACKAGE_YIDONG="移动MM";
	public static final String PACKAGE_WOSHOP="联通沃商店";
	public static final String PACKAGE_tencent_WEIBO="腾讯微博";
	
	
	
	
	
}
