package christmas.frame.photoedittor.collage.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import christmas.frame.photoedittor.collage.R;


public class DialogPolicy {
    private Dialog dialog;
    private final String contentPolicy =
            "<P STYLE=\"text-indent: 0.3in; margin-bottom: 0.11in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><B>PERSONAL\n" +
            "INFORMATION</B></FONT></FONT></P>\n" +
            "<P STYLE=\"text-indent: 0.2in; margin-bottom: 0.11in\"><A NAME=\"_GoBack\"></A>\n" +
            "<FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>Personal information\n" +
            "is data that can be used to uniquely identify or contact a single\n" +
            "person.</FONT></FONT></P>\n" +
            "<P STYLE=\"margin-bottom: 0.11in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>We\n" +
            "DO NOT collect, store or use any personal information while you\n" +
            "visit, download or upgrade our products, excepting the personal\n" +
            "information that you submit to us when you create a user account,\n" +
            "send an error report and other activities.</FONT></FONT></P>\n" +
            "<P STYLE=\"text-indent: 0.2in; margin-bottom: 0.11in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>Only\n" +
            "for the following purposes that we may use personal information\n" +
            "submitted by you: help us develop, deliver, and improve our products\n" +
            "and services and supply higher quality service; manage online surveys\n" +
            "and other activities you’ve participated in.</FONT></FONT></P>\n" +
            "<P STYLE=\"text-indent: 0.2in; margin-bottom: 0.11in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><I>In\n" +
            "the following circumstances, we may disclose your personal\n" +
            "information according to your wish or regulations by law:</I></FONT></FONT></P>\n" +
            "<P STYLE=\"margin-bottom: 0.11in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>(1)\n" +
            "Your prior permission;</FONT></FONT></P>\n" +
            "<P STYLE=\"margin-bottom: 0.11in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>(2)\n" +
            "By the applicable law within or outside your country of residence,\n" +
            "legal process, litigation requests;</FONT></FONT></P>\n" +
            "<P STYLE=\"margin-bottom: 0.11in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>(3)\n" +
            "By requests from public and governmental authorities;</FONT></FONT></P>\n" +
            "<P STYLE=\"margin-bottom: 0.11in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>(4)\n" +
            "To protect our legal rights and interests.</FONT></FONT></P>\n" +
            "<P STYLE=\"margin-bottom: 0.11in\"><BR><BR>\n" +
            "</P>\n" +
            "<P STYLE=\"text-indent: 0.3in; margin-bottom: 0.11in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3><B>NON-PERSONAL\n" +
            "INFORMATION</B></FONT></FONT></P>\n" +
            "<P STYLE=\"text-indent: 0.2in; margin-bottom: 0.11in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>Non-personal\n" +
            "information is data in a form that does not permit direct association\n" +
            "with any specific individual, such as your Android ID, CPN model,\n" +
            "memory size, phone model, rom, installed application name and package\n" +
            "name etc.</FONT></FONT></P>\n" +
            "<P STYLE=\"text-indent: 0.2in; margin-bottom: 0.11in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>We\n" +
            "may collect and use non-personal information in the following\n" +
            "circumstances. To have a better understanding in user’s behavior,\n" +
            "solve problems in products and services, improve our products,\n" +
            "services and advertising, we may collect non-personal information\n" +
            "such as installed application name and package name, the data of\n" +
            "install, country, equipment and channel.</FONT></FONT></P>\n" +
            "<P STYLE=\"text-indent: 0.2in; margin-bottom: 0.11in\"><FONT FACE=\"Times New Roman, serif\"><FONT SIZE=3>If\n" +
            "non-personal information is combined with personal information, we\n" +
            "treat the combined information as personal information for the\n" +
            "purposes of this Privacy Policy.</FONT></FONT></P>";


    public DialogPolicy() {

    }

    public Dialog buildDialog(Context context) {
        dialog = new Dialog(context,android.R.style.Theme_Material_Light_NoActionBar);
        dialog.setContentView(R.layout.dialog_policy);
        TextView textView = dialog.findViewById(R.id.tvContentPolicy);
        textView.setText(Html.fromHtml(contentPolicy));

        dialog.findViewById(R.id.btnPolicy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        return dialog;
    }


}
