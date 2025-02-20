package tv.danmaku.ijk.media.player.av3a;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class AudioVividMetaDataHandler {
    public final IjkMediaPlayer mediaPlayer;

    public static class MetaDataType<T> {
        public final Class<T> clazz;
        public final int type;
        public static final MetaDataType<Integer> AV3A_STATIC_META_CONTROL_CONTENT_NUMS = new MetaDataType<>(Integer.class, 10001);
        public static final MetaDataType<Integer> AV3A_STATIC_META_CONTROL_OBJECT_NUMS = new MetaDataType<>(Integer.class, 10002);
        public static final MetaDataType<Integer> AV3A_STATIC_META_CONTROL_PACK_NUMS = new MetaDataType<>(Integer.class, 10003);
        public static final MetaDataType<Integer> AV3A_STATIC_META_CONTROL_CHANNEL_NUMS = new MetaDataType<>(Integer.class, 10004);
        public static final MetaDataType<String> AV3A_STATIC_META_CONTROL_OBJECT_NAME = new MetaDataType<>(String.class, 10005);
        public static final MetaDataType<Integer> AV3A_STATIC_META_CONTROL_OBJECT_INTERACTION = new MetaDataType<>(Integer.class, 10006);
        public static final MetaDataType<Integer> AV3A_STATIC_META_CONTROL_OBJECT_MUTE = new MetaDataType<>(Integer.class, 10007);
        public static final MetaDataType<Float> AV3A_STATIC_META_CONTROL_OBJECT_GAIN_MIN = new MetaDataType<>(Float.class, 10008);
        public static final MetaDataType<Float> AV3A_STATIC_META_CONTROL_OBJECT_GAIN_MAX = new MetaDataType<>(Float.class, 10009);
        public static final MetaDataType<Float> AV3A_STATIC_META_CONTROL_OBJECT_AZIMUTH_MIN = new MetaDataType<>(Float.class, 10010);
        public static final MetaDataType<Float> AV3A_STATIC_META_CONTROL_OBJECT_AZIMUTH_MAX = new MetaDataType<>(Float.class, 10011);
        public static final MetaDataType<Float> AV3A_STATIC_META_CONTROL_OBJECT_ELEVATION_MIN = new MetaDataType<>(Float.class, 10012);
        public static final MetaDataType<Float> AV3A_STATIC_META_CONTROL_OBJECT_ELEVATION_MAX = new MetaDataType<>(Float.class, 10013);
        public static final MetaDataType<Float> AV3A_STATIC_META_CONTROL_OBJECT_DISTANCE_MIN = new MetaDataType<>(Float.class, 10014);
        public static final MetaDataType<Float> AV3A_STATIC_META_CONTROL_OBJECT_DISTANCE_MAX = new MetaDataType<>(Float.class, 10015);
        public static final MetaDataType<Integer> AV3A_DYNAMIC_META_CONTROL_CHANNEL_NUMS = new MetaDataType<>(Integer.class, 20001);
        public static final MetaDataType<Float> AV3A_DYNAMIC_L1_META_CONTROL_CHANNEL_GAIN = new MetaDataType<>(Float.class, 20002);
        public static final MetaDataType<Float> AV3A_DYNAMIC_L1_META_CONTROL_CHANNEL_AZIMUTH = new MetaDataType<>(Float.class, 20003);
        public static final MetaDataType<Float> AV3A_DYNAMIC_L1_META_CONTROL_CHANNEL_ELEVATION = new MetaDataType<>(Float.class, 20004);
        public static final MetaDataType<Float> AV3A_DYNAMIC_L1_META_CONTROL_CHANNEL_DISTANCE = new MetaDataType<>(Float.class, 20005);

        public MetaDataType(Class<T> cls, int i) {
            this.clazz = cls;
            this.type = i;
        }
    }

    public AudioVividMetaDataHandler(IjkMediaPlayer ijkMediaPlayer) {
        this.mediaPlayer = ijkMediaPlayer;
    }

    public <T> T getValue(MetaDataType<T> metaDataType, int i) {
        if (metaDataType.clazz.equals(Integer.class)) {
            return (T) metaDataType.clazz.cast(Integer.valueOf(this.mediaPlayer.getAv3aMetadataInt(metaDataType.type, i)));
        }
        if (metaDataType.clazz.equals(Float.class)) {
            return (T) metaDataType.clazz.cast(Float.valueOf(this.mediaPlayer.getAv3aMetadataFloat(metaDataType.type, i)));
        }
        if (metaDataType.clazz.equals(String.class)) {
            return (T) metaDataType.clazz.cast(this.mediaPlayer.getAv3aMetadataString(metaDataType.type, i));
        }
        return null;
    }

    public <T> int setValue(MetaDataType<T> metaDataType, int i, T t) {
        if (metaDataType.clazz.equals(Float.class)) {
            return this.mediaPlayer.setAv3aMetadataFloat(metaDataType.type, i, ((Float) t).floatValue());
        }
        if (metaDataType.clazz.equals(Integer.class)) {
            return this.mediaPlayer.setAv3aMetadataFloat(metaDataType.type, i, ((Integer) t).intValue());
        }
        return -1;
    }
}
