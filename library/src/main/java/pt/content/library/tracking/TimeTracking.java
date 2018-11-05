//package pt.content.library.tracking;
//
//import com.crashlytics.android.answers.Answers;
//import com.crashlytics.android.answers.CustomEvent;
//
//import java.util.HashMap;
//
//public class TimeTracking {
//    private static HashMap<String, Long> map = new HashMap<>();
//
//    public static void start(String tag) {
//        map.put(tag, System.currentTimeMillis());
//    }
//
//    public static void end(String tag) {
//        if (map.containsKey(tag)) {
//            Answers.getInstance().logCustom(new CustomEvent(tag)
//                    .putCustomAttribute("time", System.currentTimeMillis() - map.get(tag)));
//            map.remove(tag);
//        }
//    }
//
//}
