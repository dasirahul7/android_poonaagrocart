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
import com.poona.agrocart.data.network.responses.walletTransaction.TransactionType;
import com.poona.agrocart.data.network.responses.walletTransaction.TransactionTypeResponse;
import com.poona.agrocart.data.network.responses.walletTransaction.WalletTransaction;
import com.poona.agrocart.data.network.responses.walletTransaction.WalletTransactionListResponse;

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
    public MutableLiveData<ArrayList<WalletTransaction>> walletTransactionMutableList;
    public MutableLiveData<ArrayList<TransactionType>> transactionTypeMutableList;
    public WalletTransactionViewModel(@NonNull Application application) {
        super(application);
        walletBalanceMutable = new MutableLiveData<>();
        walletFromDateMutable = new MutableLiveData<>();
        walletToDateMutable = new MutableLiveData<>();
        transactionTypeMutable = new MutableLiveData<>();
        transactionTypeMutableList = new MutableLiveData<>();
        walletTransactionMutableList = new MutableLiveData<>();
        walletBalanceMutable.setValue(null);
        walletFromDateMutable.setValue(null);
        walletToDateMutable.setValue(null);
        transactionTypeMutable.setValue(null);
        transactionTypeMutableList.setValue(null);
        walletTransactionMutableList.setValue(null);
    }

    /*
    onNetworkException : priorities
    0 :getTransactionTypeApiResponse
    1 :getWalletTransactionListApiResponse
    2 :getPaymentToWalletApiResponse
   */
    /*call transaction type lis API here*/
    public LiveData<TransactionTypeResponse> getTransactionTypeResponseLiveData(ProgressDialog progressDialog,
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

    /*Call Wallet Transaction List API here*/
    public LiveData<WalletTransactionListResponse> getWalletTransactionListResponse(ProgressDialog progressDialog,
                                                                                    HashMap<String,String> hashMap,
                                                                                    WalletTransactionFragment walletTransactionFragment){
        MutableLiveData<WalletTransactionListResponse> transactionListResponseMutableLiveData = new MutableLiveData<>();
        ApiClientAuth.getClient(walletTransactionFragment.getContext())
                .create(ApiInterface.class)
                .getWalletTransactionListResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WalletTransactionListResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull WalletTransactionListResponse walletTransactionListResponse) {
                        if (walletTransactionListResponse!=null){
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            transactionListResponseMutableLiveData.setValue(walletTransactionListResponse);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        WalletTransactionListResponse transactionListResponse = new WalletTransactionListResponse();
                        try {
                            transactionListResponse = gson.fromJson(Objects.requireNonNull(Objects.requireNonNull(((HttpException) e).response()).errorBody()).string(),
                                    WalletTransactionListResponse.class);
                            transactionListResponseMutableLiveData.setValue(transactionListResponse);
                        } catch (JsonSyntaxException | IOException exception) {
                            Log.e(TAG, "onError: " + exception.getMessage());
                            ((NetworkExceptionListener) walletTransactionFragment).onNetworkException(1, "");
                        }
                    }
                });
        return transactionListResponseMutableLiveData;
    }
    /*Call Payment To Wallet API here*/
    public LiveData<BaseResponse> callPaymentToWalletApiResponse(ProgressDialog progressDialog,
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
                            ((NetworkExceptionListener) walletTransactionFragment).onNetworkException(2, "");
                        }
                    }
                });
        return baseResponseMutableLiveData;
    }
}
