package com.poona.agrocart.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.poona.agrocart.ui.home.HomeActivity;
import com.squareup.picasso.Callback;
import com.poona.agrocart.R;
import com.poona.agrocart.data.firebase.PushNotification;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.ui.splash_screen.SplashScreenActivity;
import com.poona.agrocart.widgets.blurimage.Blur;
import com.poona.agrocart.widgets.custom_alert.Alerter;
import com.poona.agrocart.widgets.toast.CustomToast;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.yalantis.ucrop.UCrop;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.LOGOUT;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public abstract class BaseFragment extends Fragment
{
    private static final String TAG = BaseFragment.class.getSimpleName();
    public Context context;

    public AppSharedPreferences preferences;

    private PushNotification pushNotification;
    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        this.context = context;

        preferences = new AppSharedPreferences(context);

        /*ordersList = new ArrayList<>();*/
        //ordersList.addAll(db.getAllOrders());

        ////////////////////list of permissions////////////////////////////
        permissions.add(CAMERA);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        ////////////////////list of permissions////////////////////////////
    }


//    Title and app logo on actionBar
protected void initTitleBar(String title) {
    ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.post(() -> {
        Drawable d = ResourcesCompat.getDrawable(getResources(),
                R.drawable.menu_icon_toggle, null);
        ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setNavigationIcon(d);
        ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setPadding(0,0,0,0);
    });
    ((HomeActivity)requireActivity()).binding.appBarHome.textTitle.setVisibility(View.VISIBLE);
    ((HomeActivity)requireActivity()).binding.appBarHome.textView.setVisibility(View.GONE);
    ((HomeActivity)requireActivity()).binding.appBarHome.logImg.setVisibility(View.GONE);
    ((HomeActivity)requireActivity()).binding.appBarHome.textTitle.setText(title);
}

    public void loadingImage(Context context, String url, ImageView imageView)
    {
        Transformation blurTransformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap blurred = Blur.fastblur(context, source, 10);
                source.recycle();
                return blurred;
            }

            @Override
            public String key() {
                return "blur()";
            }
        };

        Picasso.get()
                .load(url)
                .transform(blurTransformation)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.get()
                                .load(url) // image url goes here
                                .placeholder(imageView.getDrawable())
                                .into(imageView);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    protected void successToast(Context context, String message)
    {
        CustomToast.Config.getInstance()
                .setToastTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins/poppins_regular.ttf"))
                .apply();
        CustomToast.success(context, message, Toast.LENGTH_SHORT, true).show();
    }

    protected void errorToast(Context context, String message)
    {
        CustomToast.Config.getInstance()
                .setToastTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins/poppins_regular.ttf"))
                .apply();
        CustomToast.error(context, message, Toast.LENGTH_LONG, true).show();
    }

    protected void infoToast(Context context, String message)
    {
        CustomToast.Config.getInstance()
                .setToastTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins/poppins_regular.ttf"))
                .apply();
        CustomToast.info(context, message, Toast.LENGTH_LONG, true).show();
    }

    protected void warningToast(Context context, String message)
    {
        CustomToast.Config.getInstance()
                .setToastTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins/poppins_regular.ttf"))
                .apply();
        CustomToast.warning(context, message, Toast.LENGTH_LONG, true).show();
    }

    protected void showNotifyAlert(Activity activity, String dialogTitle, String dialogMessage, int icon) {
        Alerter.create(activity)
                .setTitle(dialogTitle)
                .setIcon(icon)
                .setDuration(5000)
                .setText(dialogMessage)
                .show();
    }

    public void hideKeyBoard(Activity activity)
    {
        View view = activity.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public ProgressDialog showCircleProgressDialog(Context context, String message)
    {
        ProgressDialog dialog = new ProgressDialog(new ContextThemeWrapper(context, R.style.CustomProgressDialog));
        try
        {
            dialog.show();
        }
        catch (WindowManager.BadTokenException e)
        {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.circular_progresbar);
        // dialog.setAmenitiesName(Message);


        // Get screen width and height in pixels
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((AppCompatActivity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        // The absolute width of the available display size in pixels.
//        int displayWidth = displayMetrics.widthPixels;
//        // The absolute height of the available display size in pixels.
//        int displayHeight = displayMetrics.heightPixels;
//
//        // Initialize a new window manager layout parameters
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//
//        // Copy the alert dialog window attributes to new layout parameter instance
//        layoutParams.copyFrom(dialog.getWindow().getAttributes());
//
//        // Set the alert dialog window width and height
//        // Set alert dialog width equal to screen width 90%
//        // int dialogWindowWidth = (int) (displayWidth * 0.9f);
//        // Set alert dialog height equal to screen height 90%
//        // int dialogWindowHeight = (int) (displayHeight * 0.9f);
//
//        // Set alert dialog width equal to screen width 70%
//        int dialogWindowWidth = (int) (displayWidth * 1.0f);
//        // Set alert dialog height equal to screen height 70%
//        int dialogWindowHeight = (int) (displayHeight * 1.0f);
//
//        // Set the width and height for the layout parameters
//        // This will bet the width and height of alert dialog
//        layoutParams.width = dialogWindowWidth;
//        layoutParams.height = dialogWindowHeight;
//
//        // Apply the newly created layout parameters to the alert dialog window
//        dialog.getWindow().setAttributes(layoutParams);

        return dialog;
    }
    
    /**
     * Method checks for Internet connectivity
     *
     * @param _context
     * @return - true - If Internet is available
     * false - If Internet is not available
     */
    protected boolean isConnectingToInternet(Context _context) {
        boolean checkConnection = false;
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the internet
            switch (activeNetwork.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                case ConnectivityManager.TYPE_MOBILE:
                    // connected to mobile data
                    // connected to wifi
                    checkConnection = true;
                    break;
                default:
                    break;
            }
        } else {
            // not connected to the internet
            checkConnection = false;
        }
        return checkConnection;
    }

    protected void goToAskSignInSignUpScreen()
    {
        preferences.clearSharedPreferences(context);
        preferences.setFromLogOut(true);

        Intent intent = new Intent(requireActivity(), SplashScreenActivity.class);
        intent.putExtra(FROM_SCREEN, LOGOUT);
        startActivity(intent);
        requireActivity().finish();
    }

    private LocationManager locationManager;
    protected boolean checkGpsStatus()
    {
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /************************************************************************************************/
    /*******Upload Camera and Gallery Image Code Start***********************************************/
    /************************************************************************************************/
    protected ArrayList<String> permissionsToRequest;
    protected ArrayList<String> permissionsRejected = new ArrayList<>();
    protected ArrayList<String> permissions = new ArrayList<>();

    protected static final int MY_CAMERA_PERMISSION = 123;
    protected static final int MY_GALLERY_REQUEST = 124;

    protected int SELECT_IMAGE_FROM_CAMERA = 0;
    protected int SELECT_IMAGE_FROM_GALLERY = 1;
    protected int SELECT_IMAGE_FOR_FC = 2; //FC = food certificate
    protected int SELECT_PDF_FOR_FC = 3; //FC = food certificate
    //protected int SELECT_IMAGE_OR_PDF_FROM_GALLERY = 2;
    private String fileName = "";

    protected void askForCameraPermissions() {
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), MY_CAMERA_PERMISSION);
            }
            if (permissionsRejected.size() > 0) {
                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), MY_CAMERA_PERMISSION);
            }
            if (permissionsToRequest.size() == 0 && permissionsRejected.size() == 0) {
                cameraIntent();
            }
        } else {
            cameraIntent();
        }
    }

    protected void askForGalleryPermissions() {
        try {
            permissionsToRequest = findUnAskedPermissions(permissions);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), MY_GALLERY_REQUEST);
                }
                if (permissionsRejected.size() > 0) {
                    requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), MY_GALLERY_REQUEST);
                }
                if (permissionsToRequest.size() == 0 && permissionsRejected.size() == 0) {
                    galleryIntent();
                }
            } else {
                galleryIntent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void askForGalleryPermissionsForPhotoPdf(String type) {
        try {
            permissionsToRequest = findUnAskedPermissions(permissions);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), MY_GALLERY_REQUEST);
                }
                if (permissionsRejected.size() > 0) {
                    requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), MY_GALLERY_REQUEST);
                }
                if (permissionsToRequest.size() == 0 && permissionsRejected.size() == 0)
                {
                    if(type.equals("photo"))
                        imageIntent();
                    else if(type.equals("pdf"))
                        pdfIntent();
                }
            }
            else
            {
                if(type.equals("photo"))
                    imageIntent();
                else if(type.equals("pdf"))
                    pdfIntent();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
            File f = new File(Environment.getExternalStorageDirectory(), fileName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), "com.poona", f));
            startActivityForResult(intent, SELECT_IMAGE_FROM_CAMERA);
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.ensure_your_all_permissions), Toast.LENGTH_SHORT).show();
        }
    }

    protected void galleryIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), SELECT_IMAGE_FROM_GALLERY);
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.ensure_your_all_permissions), Toast.LENGTH_SHORT).show();
        }
    }

    protected void imageIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), SELECT_IMAGE_FOR_FC);
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.ensure_your_all_permissions), Toast.LENGTH_SHORT).show();
        }
    }

    protected void pdfIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), SELECT_PDF_FOR_FC);
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.ensure_your_all_permissions), Toast.LENGTH_SHORT).show();
        }
    }

    protected void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        String path = "";

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(bm);
                path = getRealPathFromURI(tempUri.toString());
                File f = new File(path);

                String outputFileName = Calendar.getInstance().getTimeInMillis() + ".jpeg";
                Uri selectedImage = Uri.fromFile(f);

                /*Cropping image*/
                gotoCropImageActivity(selectedImage, Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString(), outputFileName)));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String displayFileName = null;
    protected File onSelectImageFCResult(Intent data) {
        Bitmap bm = null;
        String path = "";
        File f = null;

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri uri = getImageUri(bm);
                path = getRealPathFromURI(uri.toString());
                f = new File(path);

                String uriString = uri.toString();
                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayFileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayFileName = f.getName();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    protected File onSelectPdfFCResult(Intent data)
    {
        File myFile = null;

        // Get the Uri of the selected file

        Uri uri = data.getData();
        String uriString = uri.toString();
        String path = getRealPathFromURI(uri.toString());
        myFile = new File(uriString);

        if (uriString.startsWith("content://"))
        {
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    displayFileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        else if (uriString.startsWith("file://"))
        {
            displayFileName = myFile.getName();
        }

        return myFile;
    }

    protected void onCaptureFromCameraResult(Intent data) {
        try {
            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : Objects.requireNonNull(f.listFiles())) {
                if (temp.getName().equals(fileName)) {
                    f = temp;
                    break;
                }
            }
            if (!f.exists()) {
                Toast.makeText(getActivity(), "Error while capturing image", Toast.LENGTH_LONG).show();
                return;
            }

            /*Cropping image Start*/
            String outputFileName = Calendar.getInstance().getTimeInMillis() + ".jpeg";
            Uri selectedImage = Uri.fromFile(f);
            gotoCropImageActivity(selectedImage, Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString(), outputFileName)));
            /*Cropping image End*/
        }catch (Exception e){e.printStackTrace();}
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    protected boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private Uri getImageUri(Bitmap thumbnail) {
        String path = "";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), thumbnail, String.valueOf(Calendar.getInstance().getTimeInMillis()), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.parse(path);
    }

    protected String getRealPathFromURI(String contentURI) {
        try {
            Uri contentUri = Uri.parse(contentURI);
            Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
            if (cursor == null) {
                return contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(index);
            }
        }catch (Exception e){e.printStackTrace();}
        return contentURI;
    }

    protected Uri getFilePathFromUri(Uri uri) throws IOException
    {
        String fileName = getFileName(uri);
        File file = new File(getActivity().getExternalCacheDir(), fileName);
        file.createNewFile();
        try (OutputStream outputStream = new FileOutputStream(file);
             InputStream inputStream = getActivity().getContentResolver().openInputStream(uri))
        {
            IOUtils.copy(inputStream, outputStream); //Simply reads input to output stream
            outputStream.flush();
        }
        return Uri.fromFile(file);
    }

    protected String getFileName(Uri uri) {
        String fileName = getFileNameFromCursor(uri);
        if (fileName == null) {
            String fileExtension = getFileExtension(uri);
            fileName = "temp_file" + (fileExtension != null ? "." + fileExtension : "");
        } else if (!fileName.contains(".")) {
            String fileExtension = getFileExtension(uri);
            fileName = fileName + "." + fileExtension;
        }
        return fileName;
    }

    private String getFileExtension(Uri uri) {
        String fileType = getActivity().getContentResolver().getType(uri);
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType);
    }

    private String getFileNameFromCursor(Uri uri) {
        Cursor fileCursor = getActivity().getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);
        String fileName = null;
        if (fileCursor != null && fileCursor.moveToFirst()) {
            int cIndex = fileCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (cIndex != -1) {
                fileName = fileCursor.getString(cIndex);
            }
        }
        return fileName;
    }


    private void gotoCropImageActivity(Uri sourceUri, Uri destinationUri)
    {
        /*********This is code for fragment*********/
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(3, 2)
                .start(getContext(), this, UCrop.REQUEST_CROP);

        /*********This is code for activity*********/
//        UCrop.of(sourceUri, destinationUri)
//                .withAspectRatio(1, 1) //Perfect square crop option will show
////                .withMaxResultSize(maxWidth, maxHeight)
//                .start(getActivity());
    }
    /***********************************************************************************************/
    /*******Upload Camera and Gallery Image Code End*************************************************/
    /************************************************************************************************/



    /*******************************************************************************************/
    /*******Display pdf file Code Start*********************************************************/
    /*******************************************************************************************/

    /*protected PDFView pdfView = null;*/
    protected WebView webView = null;
    protected String pdfFileName = "Food Certificate.pdf";
    private String pdfTempFilePath = "";

    /*public void saveFileAndDisplay(File file) {
        String filePath = saveTempFileToFile(file);
        File newFile = new File(file.getPath());
        displayFromFile(newFile, pdfView);
        //final Uri uri = Uri.fromFile(newFile);
        //displayFileOnWebView(uri, webView);
    }*/

    protected void displayFileOnWebView(Uri uri, WebView webView)
    {
        WebSettings settings = webView.getSettings();
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(uri.toString());
    }


    /*animated expand view VISIBILITY VISIBLE*/
    public void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                super.applyTransformation(interpolatedTime, t);

                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density + 300));
        v.startAnimation(a);
    }

    /*animated collapse view VISIBILITY GONE*/
    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                super.applyTransformation(interpolatedTime, t);

                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density + 300));
        v.startAnimation(a);
    }

    /*private String saveTempFileToFile(File tempFile) {
        try {
            // check if the permission to write to external storage is granted
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                InputStream inputStream = new FileInputStream(tempFile);
                File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pdfFileName);
                OutputStream outputStream = new FileOutputStream(newFile);
                Util.readFromInputStreamToOutputStream(inputStream, outputStream);

                return tempFile.getPath();
            }
            else
            {
                // case if the permission hasn't been granted, we will store the pdf in a temp file
                //store the temporary file path, to be able to save it when permission will be granted
                return pdfTempFilePath;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error on file : " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private void setPdfViewConfiguration(PDFView pdfView) {
        pdfView.setMidZoom(2.0f);
        pdfView.setMaxZoom(5.0f);
    }

    private void setPageConfigurationAndLoad(PDFView.Configurator configurator) {
        configurator
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(context))
                .spacing(10) // in dp
                .onPageError(this)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
    }

    private void displayFromFile(File file, PDFView pdfView) {
        setPdfViewConfiguration(pdfView);
        setPageConfigurationAndLoad(pdfView.fromFile(file));
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onPageError(int page, Throwable t) {

    }*/

    /*******************************************************************************************/
    /*******Display pdf file Code End*********************************************************/
    /*******************************************************************************************/
}