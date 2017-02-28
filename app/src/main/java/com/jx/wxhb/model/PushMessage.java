package com.jx.wxhb.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Desc
 * Created by Jun on 2017/2/28.
 */

public class PushMessage implements Parcelable {

    /**
     * platform : all
     * audience : {"tag":["深圳","北京"]}
     * notification : {"android":{"alert":"Hi, JPush!","title":"Send to Android","builder_id":1,"extras":{"newsid":321}},"ios":{"alert":"Hi, JPush!","sound":"default","badge":"+1","extras":{"newsid":321}}}
     * message : {"msg_content":"Hi,JPush","content_type":"text","title":"msg","extras":{"key":"value"}}
     * sms_message : {"content":"sms msg content","delay_time":3600}
     * options : {"time_to_live":60,"apns_production":false}
     */

    private String platform;
    private AudienceBean audience;
    private NotificationBean notification;
    private MessageBean message;
    private SmsMessageBean sms_message;
    private OptionsBean options;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public AudienceBean getAudience() {
        return audience;
    }

    public void setAudience(AudienceBean audience) {
        this.audience = audience;
    }

    public NotificationBean getNotification() {
        return notification;
    }

    public void setNotification(NotificationBean notification) {
        this.notification = notification;
    }

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public SmsMessageBean getSms_message() {
        return sms_message;
    }

    public void setSms_message(SmsMessageBean sms_message) {
        this.sms_message = sms_message;
    }

    public OptionsBean getOptions() {
        return options;
    }

    public void setOptions(OptionsBean options) {
        this.options = options;
    }

    public static class AudienceBean implements Parcelable {
        private List<String> tag;

        public List<String> getTag() {
            return tag;
        }

        public void setTag(List<String> tag) {
            this.tag = tag;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringList(this.tag);
        }

        public AudienceBean() {
        }

        protected AudienceBean(Parcel in) {
            this.tag = in.createStringArrayList();
        }

        public static final Creator<AudienceBean> CREATOR = new Creator<AudienceBean>() {
            @Override
            public AudienceBean createFromParcel(Parcel source) {
                return new AudienceBean(source);
            }

            @Override
            public AudienceBean[] newArray(int size) {
                return new AudienceBean[size];
            }
        };
    }

    public static class NotificationBean implements Parcelable {
        /**
         * android : {"alert":"Hi, JPush!","title":"Send to Android","builder_id":1,"extras":{"newsid":321}}
         * ios : {"alert":"Hi, JPush!","sound":"default","badge":"+1","extras":{"newsid":321}}
         */

        private AndroidBean android;
        private IosBean ios;

        public AndroidBean getAndroid() {
            return android;
        }

        public void setAndroid(AndroidBean android) {
            this.android = android;
        }

        public IosBean getIos() {
            return ios;
        }

        public void setIos(IosBean ios) {
            this.ios = ios;
        }

        public static class AndroidBean implements Parcelable {
            /**
             * alert : Hi, JPush!
             * title : Send to Android
             * builder_id : 1
             * extras : {"newsid":321}
             */

            private String alert;
            private String title;
            private int builder_id;
            private ExtrasBean extras;

            public String getAlert() {
                return alert;
            }

            public void setAlert(String alert) {
                this.alert = alert;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getBuilder_id() {
                return builder_id;
            }

            public void setBuilder_id(int builder_id) {
                this.builder_id = builder_id;
            }

            public ExtrasBean getExtras() {
                return extras;
            }

            public void setExtras(ExtrasBean extras) {
                this.extras = extras;
            }

            public static class ExtrasBean implements Parcelable {
                /**
                 * newsid : 321
                 */

                private int newsid;

                public int getNewsid() {
                    return newsid;
                }

                public void setNewsid(int newsid) {
                    this.newsid = newsid;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(this.newsid);
                }

                public ExtrasBean() {
                }

                protected ExtrasBean(Parcel in) {
                    this.newsid = in.readInt();
                }

                public static final Creator<ExtrasBean> CREATOR = new Creator<ExtrasBean>() {
                    @Override
                    public ExtrasBean createFromParcel(Parcel source) {
                        return new ExtrasBean(source);
                    }

                    @Override
                    public ExtrasBean[] newArray(int size) {
                        return new ExtrasBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.alert);
                dest.writeString(this.title);
                dest.writeInt(this.builder_id);
                dest.writeParcelable(this.extras, flags);
            }

            public AndroidBean() {
            }

            protected AndroidBean(Parcel in) {
                this.alert = in.readString();
                this.title = in.readString();
                this.builder_id = in.readInt();
                this.extras = in.readParcelable(ExtrasBean.class.getClassLoader());
            }

            public static final Creator<AndroidBean> CREATOR = new Creator<AndroidBean>() {
                @Override
                public AndroidBean createFromParcel(Parcel source) {
                    return new AndroidBean(source);
                }

                @Override
                public AndroidBean[] newArray(int size) {
                    return new AndroidBean[size];
                }
            };
        }

        public static class IosBean implements Parcelable {
            /**
             * alert : Hi, JPush!
             * sound : default
             * badge : +1
             * extras : {"newsid":321}
             */

            private String alert;
            private String sound;
            private String badge;
            private ExtrasBeanX extras;

            public String getAlert() {
                return alert;
            }

            public void setAlert(String alert) {
                this.alert = alert;
            }

            public String getSound() {
                return sound;
            }

            public void setSound(String sound) {
                this.sound = sound;
            }

            public String getBadge() {
                return badge;
            }

            public void setBadge(String badge) {
                this.badge = badge;
            }

            public ExtrasBeanX getExtras() {
                return extras;
            }

            public void setExtras(ExtrasBeanX extras) {
                this.extras = extras;
            }

            public static class ExtrasBeanX implements Parcelable {
                /**
                 * newsid : 321
                 */

                private int newsid;

                public int getNewsid() {
                    return newsid;
                }

                public void setNewsid(int newsid) {
                    this.newsid = newsid;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(this.newsid);
                }

                public ExtrasBeanX() {
                }

                protected ExtrasBeanX(Parcel in) {
                    this.newsid = in.readInt();
                }

                public static final Creator<ExtrasBeanX> CREATOR = new Creator<ExtrasBeanX>() {
                    @Override
                    public ExtrasBeanX createFromParcel(Parcel source) {
                        return new ExtrasBeanX(source);
                    }

                    @Override
                    public ExtrasBeanX[] newArray(int size) {
                        return new ExtrasBeanX[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.alert);
                dest.writeString(this.sound);
                dest.writeString(this.badge);
                dest.writeParcelable(this.extras, flags);
            }

            public IosBean() {
            }

            protected IosBean(Parcel in) {
                this.alert = in.readString();
                this.sound = in.readString();
                this.badge = in.readString();
                this.extras = in.readParcelable(ExtrasBeanX.class.getClassLoader());
            }

            public static final Creator<IosBean> CREATOR = new Creator<IosBean>() {
                @Override
                public IosBean createFromParcel(Parcel source) {
                    return new IosBean(source);
                }

                @Override
                public IosBean[] newArray(int size) {
                    return new IosBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.android, flags);
            dest.writeParcelable(this.ios, flags);
        }

        public NotificationBean() {
        }

        protected NotificationBean(Parcel in) {
            this.android = in.readParcelable(AndroidBean.class.getClassLoader());
            this.ios = in.readParcelable(IosBean.class.getClassLoader());
        }

        public static final Creator<NotificationBean> CREATOR = new Creator<NotificationBean>() {
            @Override
            public NotificationBean createFromParcel(Parcel source) {
                return new NotificationBean(source);
            }

            @Override
            public NotificationBean[] newArray(int size) {
                return new NotificationBean[size];
            }
        };
    }

    public static class MessageBean implements Parcelable {
        /**
         * msg_content : Hi,JPush
         * content_type : text
         * title : msg
         * extras : {"key":"value"}
         */

        private String msg_content;
        private String content_type;
        private String title;
        private ExtrasBeanXX extras;

        public String getMsg_content() {
            return msg_content;
        }

        public void setMsg_content(String msg_content) {
            this.msg_content = msg_content;
        }

        public String getContent_type() {
            return content_type;
        }

        public void setContent_type(String content_type) {
            this.content_type = content_type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public ExtrasBeanXX getExtras() {
            return extras;
        }

        public void setExtras(ExtrasBeanXX extras) {
            this.extras = extras;
        }

        public static class ExtrasBeanXX implements Parcelable {
            /**
             * key : value
             */

            private String key;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.key);
            }

            public ExtrasBeanXX() {
            }

            protected ExtrasBeanXX(Parcel in) {
                this.key = in.readString();
            }

            public static final Creator<ExtrasBeanXX> CREATOR = new Creator<ExtrasBeanXX>() {
                @Override
                public ExtrasBeanXX createFromParcel(Parcel source) {
                    return new ExtrasBeanXX(source);
                }

                @Override
                public ExtrasBeanXX[] newArray(int size) {
                    return new ExtrasBeanXX[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.msg_content);
            dest.writeString(this.content_type);
            dest.writeString(this.title);
            dest.writeParcelable(this.extras, flags);
        }

        public MessageBean() {
        }

        protected MessageBean(Parcel in) {
            this.msg_content = in.readString();
            this.content_type = in.readString();
            this.title = in.readString();
            this.extras = in.readParcelable(ExtrasBeanXX.class.getClassLoader());
        }

        public static final Creator<MessageBean> CREATOR = new Creator<MessageBean>() {
            @Override
            public MessageBean createFromParcel(Parcel source) {
                return new MessageBean(source);
            }

            @Override
            public MessageBean[] newArray(int size) {
                return new MessageBean[size];
            }
        };
    }

    public static class SmsMessageBean implements Parcelable {
        /**
         * content : sms msg content
         * delay_time : 3600
         */

        private String content;
        private int delay_time;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getDelay_time() {
            return delay_time;
        }

        public void setDelay_time(int delay_time) {
            this.delay_time = delay_time;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.content);
            dest.writeInt(this.delay_time);
        }

        public SmsMessageBean() {
        }

        protected SmsMessageBean(Parcel in) {
            this.content = in.readString();
            this.delay_time = in.readInt();
        }

        public static final Creator<SmsMessageBean> CREATOR = new Creator<SmsMessageBean>() {
            @Override
            public SmsMessageBean createFromParcel(Parcel source) {
                return new SmsMessageBean(source);
            }

            @Override
            public SmsMessageBean[] newArray(int size) {
                return new SmsMessageBean[size];
            }
        };
    }

    public static class OptionsBean implements Parcelable {
        /**
         * time_to_live : 60
         * apns_production : false
         */

        private int time_to_live;
        private boolean apns_production;

        public int getTime_to_live() {
            return time_to_live;
        }

        public void setTime_to_live(int time_to_live) {
            this.time_to_live = time_to_live;
        }

        public boolean isApns_production() {
            return apns_production;
        }

        public void setApns_production(boolean apns_production) {
            this.apns_production = apns_production;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.time_to_live);
            dest.writeByte(this.apns_production ? (byte) 1 : (byte) 0);
        }

        public OptionsBean() {
        }

        protected OptionsBean(Parcel in) {
            this.time_to_live = in.readInt();
            this.apns_production = in.readByte() != 0;
        }

        public static final Creator<OptionsBean> CREATOR = new Creator<OptionsBean>() {
            @Override
            public OptionsBean createFromParcel(Parcel source) {
                return new OptionsBean(source);
            }

            @Override
            public OptionsBean[] newArray(int size) {
                return new OptionsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.platform);
        dest.writeParcelable(this.audience, flags);
        dest.writeParcelable(this.notification, flags);
        dest.writeParcelable(this.message, flags);
        dest.writeParcelable(this.sms_message, flags);
        dest.writeParcelable(this.options, flags);
    }

    public PushMessage() {
    }

    protected PushMessage(Parcel in) {
        this.platform = in.readString();
        this.audience = in.readParcelable(AudienceBean.class.getClassLoader());
        this.notification = in.readParcelable(NotificationBean.class.getClassLoader());
        this.message = in.readParcelable(MessageBean.class.getClassLoader());
        this.sms_message = in.readParcelable(SmsMessageBean.class.getClassLoader());
        this.options = in.readParcelable(OptionsBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<PushMessage> CREATOR = new Parcelable.Creator<PushMessage>() {
        @Override
        public PushMessage createFromParcel(Parcel source) {
            return new PushMessage(source);
        }

        @Override
        public PushMessage[] newArray(int size) {
            return new PushMessage[size];
        }
    };
}
