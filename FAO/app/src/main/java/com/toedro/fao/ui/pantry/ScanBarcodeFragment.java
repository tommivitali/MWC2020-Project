package com.toedro.fao.ui.pantry;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.button.MaterialButton;
import com.toedro.fao.App;
import com.toedro.fao.R;
import com.toedro.fao.db.Pantry;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ScanBarcodeFragment extends Fragment {

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private TextView barcodeText;
    private MaterialButton nextButton;
    private String barcodeData;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scan_barcode, container, false);

        surfaceView = v.findViewById(R.id.surface_view);
        barcodeText = v.findViewById(R.id.barcode_text);
        nextButton = v.findViewById(R.id.buttonAdd);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadFragment();
            }
        });
        barcodeText.setVisibility(View.INVISIBLE);

        return v;
    }

    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(width, height)//.setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeDetector.release();
                    barcodeText.post(new Runnable() {
                        @Override
                        public void run() {
                            barcodeText.setVisibility(View.VISIBLE);
                            cameraSource.stop();
                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);

                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try  {

                                            URL url = new URL("https://world.openfoodfacts.org/api/v0/product/"+barcodeData+".json");
                                            String content = ReadURL("https://world.openfoodfacts.org/api/v0/product/"+barcodeData+".json");

                                            Log.d("prova_content", content);
                                            JSONObject root = new JSONObject(content);
                                            String barcode = root.getString("code");
                                            Log.d("prova_code", barcode);

                                            JSONObject product = root.getJSONObject("product");
                                            JSONObject nutriments = product.getJSONObject("nutriments");
                                            Integer energy_100g = nutriments.getInt("energy-kcal_100g");
                                            Log.d("nutriments", energy_100g.toString());
                                            String name = product.getString("product_name_fr");
                                            Log.d("name", name);
                                            String quantity1 = product.getString("quantity");

                                            Log.d("quantity", quantity1);
                                            quantity1 = quantity1.substring(0,quantity1.indexOf(' '));
                                            Integer quantity = Integer.parseInt(quantity1);
                                            Log.d("quantity", quantity.toString());
                                            //String keywords = jsonObject.get("isbn"); creare tabella
                                            String image = product.getString("image_front_url");
                                            Log.d("image", image);
                                            //TODO associate name to keywords
                                            Pantry pantries = new Pantry(name, quantity, energy_100g, name, barcode, image);
                                            //Pantry pantry = App.getDBInstance().pantryDAO().getPantry(name, barcode);
                                            //if(pantry == null) {
                                            App.getDBInstance().pantryDAO().addPantry(pantries);
                                        } catch (Exception e) {
                                            Log.d("prova", "errore");
                                        }

                                    }
                                });

                                thread.start();

                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
    public static String ReadURL(String URL) throws Exception{
        URL test = new URL(URL);
        URLConnection uc = test.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc
                .getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }

        in.close();
        return(sb.toString());
    }

    private void reloadFragment(){
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
        if(currentFragment != null) {
            FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();
        }else {
            Toast.makeText(getContext(),"null", Toast.LENGTH_LONG).show();
            Navigation.findNavController(getView()).navigate(R.id.action_scanBarcodeFragment_self);
        }
    }

}

