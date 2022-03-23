package com.poona.agrocart.ui.nav_wallet;

import android.app.Application;
import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.walletTransaction.TransactionTypeResponse;
import com.poona.agrocart.data.network.responses.walletTransaction.WalletTransactionType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class WalletTransactionViewModel extends AndroidViewModel {
    private static final String TAG = WalletTransactionViewModel.class.getSimpleName();
    public MutableLiveData<String> walletBalanceMutable;
    public MutableLiveData<String> walletFromDateMutable;
    public MutableLiveData<String> walletToDateMutable;
    public MutableLiveData<String> transactionTypeMutable;
    public MutableLiveData<ArrayList<WalletTransactionType>> walletTransactionTypeListMutable;
    public WalletTransactionViewModel(@NonNull Application application) {
        super(application);
        walletBalanceMutable = new MutableLiveData<>();
        walletFromDateMutable = new MutableLiveData<>();
        walletToDateMutable = new MutableLiveData<>();
        transactionTypeMutable = new MutableLiveData<>();
        walletTransactionTypeListMutable = new MutableLiveData<>();
        walletBalanceMutable.setValue(null);
        walletFromDateMutable.setValue(null);
        walletToDateMutable.setValue(null);
        transactionTypeMutable.setValue(null);
        walletTransactionTypeListMutable.setValue(null);
    }

    /*
    onNetworkException : priorities
    0 :getTransactionTypeApiResponse
    1 :getPaymentToWalletApiResponse
   */
    /*call transaction type lis API here*/
    public LiveData<TransactionTypeResponse> transactionTypeResponseLiveData(ProgressDialog progressDialog,
                                                                             WalletTransactionFragment walletTransactionFragment){
        MutableLiveData<TransactionTypeResponse> transactionTypeResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(walletTransactionFragment.getContext())
                .create(ApiInterface.class)
                .getTransactionTypeApiResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<TransactionTypeResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull TransactionTypeResponse transactionTypeResponse) {
                        if (transactionTypeResponse!=null){
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            transactionTypeResponseMutableLiveData.setValue(transactionTypeResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        TransactionTypeResponse transactionTypeResponse = new TransactionTypeResponse();
                        try {
                            transactionTypeResponse = gson.fromJson(Objects.requireNonNull(Objects.requireNonNull(((HttpException) e).response()).errorBody()).string(),
                                    TransactionTypeResponse.class);
                            transactionTypeResponseMutableLiveData.setValue(transactionTypeResponse);
                        } catch (JsonSyntaxException | IOException exception) {
                            Log.e(TAG, "onError: " + exception.getMessage());
                            ((NetworkExceptionListener) walletTransactionFragment).onNetworkException(0, "");
                        }
                    }
                });
        return transactionTypeResponseMutableLiveData;
    }

    /*Call Payment To Wallet API here*/
    public LiveData<BaseResponse> addToWalletApiResponse(ProgressDialog progressDialog,
                                                         HashMap<String,String> hashMap,
                                                         WalletTransactionFragment walletTransactionFragment){
        MutableLiveData<BaseResponse> baseResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(walletTransactionFragment.context)
                .create(ApiInterface.class)
                .paymentToWallet(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BaseResponse response) {
                        if (response!=null){
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            baseResponseMutableLiveData.setValue(response);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        BaseResponse baseResponse = new BaseResponse();
                        try {
                            baseResponse = gson.fromJson(Objects.requireNonNull(Objects.requireNonNull(((HttpException) e).response()).errorBody()).string(),
                                    BaseResponse.class);
                            baseResponseMutableLiveData.setValue(baseResponse);
                        } catch (JsonSyntaxException | IOException exception) {
                            Log.e(TAG, "onError: " + exception.getMessage());
                            ((NetworkExceptionListener) walletTransactionFragment).onNetworkException(1, "");
                        }
                    }
                });
        return baseResponseMutableLiveData;
    }
}
