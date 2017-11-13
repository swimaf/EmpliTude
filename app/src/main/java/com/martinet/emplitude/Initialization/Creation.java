package com.martinet.emplitude.Initialization;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by martinet on 28/09/16.
 */

public interface Creation {
    void onCreate(Bundle bundle);
    View.OnClickListener onRevert();
    View.OnClickListener onValidate();
    Button getBtnNext();
    Button getBtnPrevious();
    FragmentActivity getFragmentActivity();

}
