package com.yikang.heartmark.common.business.other;

import com.yikang.heartmark.constant.Constants;
import com.yikang.heartmark.model.chat.Media;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by guolchen on 2014/12/15.
 */
public class CommonManager {

    private CommonManager() {
    }

    private static Logger logger = LoggerFactory.getLogger(CommonManager.class);

    static class CommonManagerHolder {
        static CommonManager commonManager = new CommonManager();
    }

    public static CommonManager getInstance() {
        return CommonManagerHolder.commonManager;
    }


//    /**
//     * 获取省份直辖市列表
//     *
//     * @param context
//     * @param dataHandler
//     */
//    public void getProvincesList(final Context context, final DataHandler<JSONObject> dataHandler) {
//        try {
//            if (!CheckNetUtil.checkNetWork(context)) {
//                if (dataHandler != null) {
//                    dataHandler.onData(Constants.Error.NETWORK_IS_UNREACHABLE, "network is unreachable", null);
//                }
//                return;
//            }
//            final HttpServiceHandler httpServiceHandler = new HttpServiceHandler<JSONObject>(dataHandler) {
//                @Override
//                public void onResponse(int code, String reason, JSONObject msgData) {
//                    if (code == Constants.SERVER_SUCCESS) {
//                        try {
//                            if (null != getDataHandler()) {
//                                getDataHandler().onData(code, reason, msgData);
//                            }
//                        } catch (Exception e) {
//                            logger.error(e.toString(), e);
//                        }
//                    }
//                }
//
//                @Override
//                public void onError(int code, String reason, Throwable e) {
//                    super.onError(code, reason, e);
//                    logger.error("code=" + String.valueOf(code) + ",resson=" + reason);
//                    getDataHandler().onData(code, reason, null);
//                }
//            };
//
//            String urlService = AppContext.getAppContext().getServiceAddress(Constants.SERVICE_ADDRESS_NAME);
//            String url = urlService + "/v1/pub_common/provinces/list";
//
//            //httpCommonDispatch(Constants.CommonOperationType.PROVINCE, url, context, null, dataHandler);
//            HttpRequestUtil.sendRequestAsync(url, null, context, httpServiceHandler);
//        } catch (Exception e) {
//            logger.error(e.toString(), e);
//        }
//    }
//
//    public void getHospitalList(final Context context, String provinceCode, final DataHandler<JSONObject> dataHandler) {
//        try {
//            if (!CheckNetUtil.checkNetWork(context)) {
//                if (dataHandler != null) {
//                    dataHandler.onData(Constants.Error.NETWORK_IS_UNREACHABLE, "network is unreachable", null);
//                }
//                return;
//            }
//            final HttpServiceHandler httpServiceHandler = new HttpServiceHandler<JSONObject>(dataHandler) {
//                @Override
//                public void onResponse(int code, String reason, JSONObject msgData) {
//                    if (code == Constants.SERVER_SUCCESS) {
//                        try {
//                            if (null != getDataHandler()) {
//                                getDataHandler().onData(code, reason, msgData);
//                            }
//                        } catch (Exception e) {
//                            logger.error(e.toString(), e);
//                        }
//                    }
//                }
//
//                @Override
//                public void onError(int code, String reason, Throwable e) {
//                    super.onError(code, reason, e);
//                    logger.error("code=" + String.valueOf(code) + ",resson=" + reason);
//                    getDataHandler().onData(code, reason, null);
//                }
//            };
//
//            String urlService = AppContext.getAppContext().getServiceAddress(Constants.SERVICE_ADDRESS_NAME);
//            String url = urlService + "/v1/pub_common/hospitals/list";
//            final JSONObject jsonObj = new JSONObject();
//            JSONUtils.addJSONParam(jsonObj, "province_code", provinceCode);
//            //httpCommonDispatch(Constants.CommonOperationType.HOSPITAL, url, context, jsonObj, dataHandler);
//            HttpRequestUtil.sendRequestAsync(url, jsonObj, context, httpServiceHandler);
//        } catch (Exception e) {
//            logger.error(e.toString(), e);
//        }
//    }
//
//    public void getDepartmentList(final Context context, int hospitalId, final DataHandler<JSONObject> dataHandler) {
//        try {
//            if (!CheckNetUtil.checkNetWork(context)) {
//                if (dataHandler != null) {
//                    dataHandler.onData(Constants.Error.NETWORK_IS_UNREACHABLE, "network is unreachable", null);
//                }
//                return;
//            }
//            final HttpServiceHandler httpServiceHandler = new HttpServiceHandler<JSONObject>(dataHandler) {
//                @Override
//                public void onResponse(int code, String reason, JSONObject msgData) {
//                    if (code == Constants.SERVER_SUCCESS) {
//                        try {
//                            if (null != getDataHandler()) {
//                                getDataHandler().onData(code, reason, msgData);
//                            }
//                        } catch (Exception e) {
//                            logger.error(e.toString(), e);
//                        }
//                    }
//                }
//
//                @Override
//                public void onError(int code, String reason, Throwable e) {
//                    super.onError(code, reason, e);
//                    logger.error("code=" + String.valueOf(code) + ",resson=" + reason);
//                    getDataHandler().onData(code, reason, null);
//                }
//            };
//
//            String urlService = AppContext.getAppContext().getServiceAddress(Constants.SERVICE_ADDRESS_NAME);
//            String url = urlService + "/v1/pub_common/departments/list";
//            final JSONObject jsonObj = new JSONObject();
//            JSONUtils.addJSONParam(jsonObj, "hospital_id", hospitalId);
//            //httpCommonDispatch(Constants.CommonOperationType.DEPARTMENT, url, context, jsonObj, dataHandler);
//            HttpRequestUtil.sendRequestAsync(url, jsonObj, context, httpServiceHandler);
//        } catch (Exception e) {
//            logger.error(e.toString(), e);
//        }
//    }
//
//    public ResultData syncUploadProfilePic(final String filePath, final int imageStyle) {
//        Media media = MessageBuilder.buildImageMedia(filePath, imageStyle, 0);
//        Bitmap bitmap = BimpUtils.resizeImageWithSize(media.getFullName(), 720, 720);
//        media.setContent(BimpUtils.compressImage2Bytes(bitmap));
//        try {
//            ResultData map = MediaManager.getInstance().uploadFile(media);
//            if (map.getCode() == Constants.SERVER_SUCCESS) {
//                return map;
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage().toString());
//        }
//        return null;
//    }
//
//    public void AddHospital(final Context context, final String hospitalName, final String locationCode, final int grade, final String address, final String departmentName, final DataHandler<HospitalAndDepartment> dataHandler) {
//        try {
//            if (!CheckNetUtil.checkNetWork(context)) {
//                if (dataHandler != null) {
//                    dataHandler.onData(Constants.Error.NETWORK_IS_UNREACHABLE, "network is unreachable", null);
//                }
//                return;
//            }
//            final HttpServiceHandler httpServiceHandler = new HttpServiceHandler<HospitalAndDepartment>(dataHandler) {
//                @Override
//                public void onResponse(int code, String reason, JSONObject msgData) {
//                    if (code == Constants.SERVER_SUCCESS) {
//                        try {
//                            if (null != getDataHandler()) {
//                                HospitalAndDepartment hospitalAndDepartment = new HospitalAndDepartment();
//                                hospitalAndDepartment.setHospitalId(msgData.optInt("hospital_id"));
//                                hospitalAndDepartment.setDepartmentId(msgData.optInt("department_id"));
//                                hospitalAndDepartment.setHospitalName(hospitalName);
//                                hospitalAndDepartment.setDepartmentName(departmentName);
//                                hospitalAndDepartment.setGrade(grade);
//                                hospitalAndDepartment.setLocationCode(locationCode);
//                                hospitalAndDepartment.setAddress(address);
//                                getDataHandler().onData(code, reason, hospitalAndDepartment);
//                            }
//                        } catch (Exception e) {
//                            logger.error(e.toString(), e);
//                        }
//                    }
//                }
//
//                @Override
//                public void onError(int code, String reason, Throwable e) {
//                    super.onError(code, reason, e);
//                    logger.error("code=" + String.valueOf(code) + ",resson=" + reason);
//                    getDataHandler().onData(code, reason, null);
//                }
//            };
//
//            String urlService = AppContext.getAppContext().getServiceAddress(Constants.SERVICE_ADDRESS_NAME);
//            String url = urlService + "/v1/pub_common/hospitals/add";
//            final JSONObject jsonObj = new JSONObject();
//            JSONUtils.addJSONParam(jsonObj, "hospital_name", hospitalName);
//            JSONUtils.addJSONParam(jsonObj, "location_code", locationCode);
//            JSONUtils.addJSONParam(jsonObj, "grade", grade);
//            JSONUtils.addJSONParam(jsonObj, "address", address);
//            JSONUtils.addJSONParam(jsonObj, "department_name", departmentName);
//
//            HttpRequestUtil.sendRequestAsync(url, jsonObj, context, httpServiceHandler);
//        } catch (Exception e) {
//            logger.error(e.toString(), e);
//        }
//    }
//
//    public void suggest(final Context context, long orginalId, String content, String contact, int flag, final DataHandler<JSONObject> dataHandler) {
//        try {
//            if (!CheckNetUtil.checkNetWork(context)) {
//                if (dataHandler != null) {
//                    dataHandler.onData(Constants.Error.NETWORK_IS_UNREACHABLE, "network is unreachable", null);
//                }
//                return;
//            }
//            final HttpServiceHandler httpServiceHandler = new HttpServiceHandler<JSONObject>(dataHandler) {
//                @Override
//                public void onResponse(int code, String reason, JSONObject msgData) {
//                    if (code == Constants.SERVER_SUCCESS) {
//                        try {
//                            if (null != getDataHandler()) {
//                                getDataHandler().onData(code, reason, msgData);
//                            }
//                        } catch (Exception e) {
//                            logger.error(e.toString(), e);
//                        }
//                    }
//                }
//
//                @Override
//                public void onError(int code, String reason, Throwable e) {
//                    super.onError(code, reason, e);
//                    logger.error("code=" + String.valueOf(code) + ",resson=" + reason);
//                    getDataHandler().onData(code, reason, null);
//                }
//            };
//
//            String urlService = AppContext.getAppContext().getServiceAddress(Constants.SERVICE_ADDRESS_NAME);
//            String url = urlService + "/v1/suggestion/suggest";
//            final JSONObject jsonObj = new JSONObject();
//            JSONUtils.addJSONParam(jsonObj, "original_id", String.valueOf(orginalId));
//            JSONUtils.addJSONParam(jsonObj, "content", content);
//            JSONUtils.addJSONParam(jsonObj, "contact", contact);
//            JSONUtils.addJSONParam(jsonObj, "flag", flag);
//            if (AppContext.getAppContext().getApplicationType() == Constants.AppTypes.DOCTOR) {
//                JSONUtils.addJSONParam(jsonObj, "product_name", "health-doctor");
//            } else {
//                JSONUtils.addJSONParam(jsonObj, "product_name", "health-patient");
//            }
//            DeviceInfo deviceInfo = AppContext.getAppContext().getDeviceInfo();
//
//            JSONUtils.addJSONParam(jsonObj, Constants.OS_VERSION, deviceInfo.getOsVersion());
//            JSONUtils.addJSONParam(jsonObj, Constants.DEVICE_TYPE, deviceInfo.getDeviceType());
//            JSONUtils.addJSONParam(jsonObj, Constants.CLIENT_VERSION, deviceInfo.getClientVersion());
//
//            HttpRequestUtil.sendRequestAsync(url, jsonObj, context, httpServiceHandler);
//        } catch (Exception e) {
//            logger.error(e.toString(), e);
//        }
//    }
//
//
//    public void getCounts(Context context) {
//        if (AppContext.getAppContext().getCurrentUser() == null)
//            return;
//
//        if (AppContext.getAppContext().getApplicationType() == Constants.AppTypes.DOCTOR) {
//            DoctorManager.getInstance().getCounts(context, ((Doctor) AppContext.getAppContext().getCurrentUser()).getDoctorId());
//        } else {
//            PatientManager.getInstance().getCounts(context, ((Patient) AppContext.getAppContext().getCurrentUser()).getPatientId());
//        }
//
//    }

    public String getMediaObjectFile(Media media, int mediaObjectType) {

        String fileUrl = null;
        String objectId = null;
        String objectFile = null;

        if (mediaObjectType == Constants.MediaObjectTypes.OBJECT) {
            objectId = media.getObjectId();
            objectFile = media.getLocalObjectFileName();
        } else if (mediaObjectType == Constants.MediaObjectTypes.THUMBNAIL) {
            objectId = media.getThumbnailImageId();
            objectFile = media.getLocalThumbnailFileName();
        } else if (mediaObjectType == Constants.MediaObjectTypes.LARGE) {
            objectId = media.getLargeImageId();
            objectFile = media.getLocalLargeImageFileName();
        }

        if (objectId == null || objectId.isEmpty() || objectFile == null || objectFile.isEmpty()) {
            logger.error("getMediaObjectFile", "ojbectID or objectFile is null!");
            return null;
        }

        objectFile = StorageManager.getInstance().getFullFileName(objectFile, media.getSessionId());
        if (StorageManager.getInstance().isExists(objectFile, false)) {
            //file://filename
            fileUrl = StorageManager.getInstance().getFormatFileName(objectFile);
        } else {
            //http://aasassadas/aasas
            fileUrl = AppCommonService.getInstance().getObjectDownloadUrl(objectId);
        }

        return fileUrl;
    }

//    public List<Location> getLocations() {
//        try {
//            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
//            String sql = "SELECT * FROM Location";
//            String[] params = new String[]{};
//
//            List<Location> locations = queryRunner.query(sql, new DbQueryRunner.RowHandler() {
//                @Override
//                public Object handle(Cursor c) {
//                    Location location = new Location();
//                    location.setLocationCode(c.getString(c.getColumnIndex("LocationCode")));
//                    location.setExternalCode(c.getString(c.getColumnIndex("ExternalCode")));
//                    location.setDescription(c.getString(c.getColumnIndex("Description")));
//                    return location;
//                }
//            }, params);
//
//            return locations;
//        } catch (Exception e) {
//            logger.error(e.toString(), e);
//        }
//        return null;
//    }
//
//    public String getLocation(String locationCode) {
//        try {
//            DbQueryRunner queryRunner = DbQueryRunner.getInstance();
//            String sql = "SELECT Description FROM Location WHERE LocationCode=? ";
//            String[] params = new String[]{locationCode};
//
//            List<String> locationName = queryRunner.query(sql, new DbQueryRunner.RowHandler() {
//                @Override
//                public Object handle(Cursor c) {
//                    String name = c.getString(c.getColumnIndex("Description"));
//                    return name;
//                }
//            }, params);
//            if (locationName.size() > 0) {
//                return locationName.get(0);
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            logger.error(e.toString(), e);
//        }
//        return null;
//    }
//
//    public void checkCall(final Context context, String doctorId, String patientId, int direction, final DataHandler<JSONObject> dataHandler) {
//        try {
//            final HttpServiceHandler httpServiceHandler = new HttpServiceHandler<JSONObject>(dataHandler) {
//                @Override
//                public void onResponse(int code, String reason, JSONObject msgData) {
//                    if (code == Constants.SERVER_SUCCESS) {
//                        try {
//                            if (null != getDataHandler()) {
//                                getDataHandler().onData(code, reason, msgData);
//                            }
//                        } catch (Exception e) {
//                            logger.error(e.toString(), e);
//                        }
//                    } else {
//                        if (null != getDataHandler()) {
//                            getDataHandler().onData(code, reason, msgData);
//                        }
//                    }
//                }
//
//                @Override
//                public void onError(int code, String reason, Throwable e) {
//                    super.onError(code, reason, e);
//                    logger.error("code=" + String.valueOf(code) + ",resson=" + reason);
//                    if (null != getDataHandler()) {
//                        getDataHandler().onData(code, reason, null);
//                    }
//                }
//            };
//
//            String urlService = AppContext.getAppContext().getServiceAddress(Constants.SERVICE_ADDRESS_NAME);
//            String url = urlService + "/v1/call/prepare";
//            final JSONObject jsonObj = new JSONObject();
//
//            JSONUtils.addJSONParam(jsonObj, "doctor_id", doctorId);
//            JSONUtils.addJSONParam(jsonObj, "patient_id", patientId);
//            JSONUtils.addJSONParam(jsonObj, "call_type", direction); //1病人打给医生 2医生打给病人
//
//            HttpRequestUtil.sendRequestAsync(url, jsonObj, context, httpServiceHandler);
//        } catch (Exception e) {
//            logger.error(e.toString(), e);
//        }
//    }
}
