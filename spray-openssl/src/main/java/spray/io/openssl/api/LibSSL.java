package spray.io.openssl.api;

import org.bridj.*;
import org.bridj.ann.Library;
import org.bridj.ann.Ptr;

@Library("ssl")
public class LibSSL {
    static {
        BridJ.register();
    }

    public static native long BIO_new(long method);
    public static native int BIO_free(@Ptr long bio);
    public static native int BIO_new_bio_pair(
        Pointer<Long> bio1,
        @Ptr long writebuf1,
        Pointer<Long> bio2,
        @Ptr long writebuf2);

    public static native int BIO_write(@Ptr long bio, long wbuf, int wlen);
    public static native int BIO_read(@Ptr long bio, long wbuf, int wlen);
    public static native int BIO_set_flags(@Ptr long bio, int flags);
    public static native int BIO_ctrl_pending(@Ptr long bio);

    public static native int SSL_library_init();

    public static native long SSLv23_method();
    public static native long SSLv23_client_method();

    public static native long SSL_CTX_new(@Ptr long method);
    public static native int SSL_CTX_set_default_verify_paths(@Ptr long ctx);
    public static native void SSL_CTX_set_verify(@Ptr long ctx, int mode, long callback);
                                //int (*verify_callback)(int, X509_STORE_CTX *));
    public static native int SSL_CTX_set_cipher_list(@Ptr long ctx, long pStr);
    public static native int SSL_CTX_use_PrivateKey_file(@Ptr long ctx, Pointer<Byte> fileName, int type);
    public static native int SSL_CTX_use_certificate_chain_file(@Ptr long ctx, Pointer<Byte> fileName);
    public static native X509_STORE SSL_CTX_get_cert_store(@Ptr long ctx);

    public static native void SSL_CTX_sess_set_new_cb(@Ptr long ctx, Pointer<NewSessionCB> callback);

    public static abstract class NewSessionCB extends Callback<NewSessionCB> {
        abstract public void apply(SSL ssl, SSL_SESSION session);
    }

    public static native long SSL_CTX_ctrl(@Ptr long ctx, int cmd, long larg, long parg);

    public static native int SSL_CTX_get_ex_new_index(long argl,
                                                  long argp,
                                                  long new_func,
                                                  long dup_func,
                                                  Pointer<CRYPTO_EX_free> free_func);
    public static native int SSL_CTX_set_ex_data(@Ptr long ctx, int idx, long arg);
    public static native long SSL_CTX_get_ex_data(@Ptr long ctx, int idx);

    public static native long SSL_new(@Ptr long ctx);

    public static native void SSL_free(@Ptr long ssl);

    public static native SSLCtx SSL_get_SSL_CTX(SSL ssl);

    public static native void SSL_set_bio(
        @Ptr long ssl,
        long rbio,
        long wbio);

    public static native int SSL_connect(@Ptr long ssl);
    public static native int SSL_accept(@Ptr long ssl);
    public static native void SSL_set_accept_state(@Ptr long ssl);

    public static native int SSL_write(@Ptr long ssl, long wbuf, int wlen);
    public static native int SSL_read(@Ptr long ssl, long wbuf, int wlen);

    public static native int SSL_want(@Ptr long ssl);
    public static native int SSL_pending(@Ptr long ssl);

    public static native int SSL_set_session(SSL ssl, SSL_SESSION session);

    public static native int SSL_get_error(@Ptr long ssl, int ret);
    public static native int SSL_set_info_callback(@Ptr long ssl, Pointer<InfoCallback> callback);

    public static abstract class InfoCallback extends Callback<InfoCallback> {
        abstract public void apply(/*Pointer<SSL>*/ long ssl, int where, int ret);
    }

    public static native int SSL_get_ex_new_index(long argl,
                                                  long argp,
                                                  long new_func,
                                                  long dup_func,
                                                  Pointer<CRYPTO_EX_free> free_func);
    public static native int SSL_set_ex_data(@Ptr long ssl, int idx, long arg);
    public static native long SSL_get_ex_data(@Ptr long ssl, int idx);

    public static abstract class CRYPTO_EX_free extends Callback<CRYPTO_EX_free> {
        abstract public void apply(@Ptr long parent, long data, long cryptoExData,
                                    int idx, long argl, long argp);
    }


    public static native SSL_SESSION SSL_get1_session(@Ptr long ssl);
    public static native long SSL_SESSION_get_time(SSL_SESSION session);
    public static native SSL_SESSION d2i_SSL_SESSION(@Ptr long a, Pointer<Pointer<Byte>> in, long length);
    public static native int i2d_SSL_SESSION(SSL_SESSION in, Pointer<Pointer<Byte>> out);

    public static native void SSL_load_error_strings();
    public static native long ERR_get_error();
    public static native void ERR_error_string_n(long err, Pointer<Byte> buffer, int len);

    public static final int BIO_CTRL_FLUSH = 11;

    public native static int CRYPTO_num_locks();
	public native static void CRYPTO_set_locking_callback(Pointer<LockingCB> arg);
	public native static int CRYPTO_THREADID_set_callback(Pointer<ThreadIdCB> arg);
	public native static void CRYPTO_THREADID_set_numeric(long ptr, long val);

    public static native X509Certificate d2i_X509_bio(BIO bio, @Ptr long resPtr);

    public static native long d2i_PKCS8_PRIV_KEY_INFO_bio(BIO bio, long retPtr);
    public static native EVP_PKEY EVP_PKCS82PKEY(@Ptr long pkcs8Key);

    public static native int X509_STORE_add_cert(X509_STORE store, X509Certificate x509);
}