import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pakmail.Detalle
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class Codigoqr : Fragment(), ZXingScannerView.ResultHandler {
    companion object {
        fun newInstance(): Codigoqr = Codigoqr()
    }

    private var mScannerView: ZXingScannerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mScannerView = ZXingScannerView(activity)
//        titulo.text = "Escanear producto"
        return mScannerView
    }

    override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera()
    }

    override fun handleResult(rawResult: Result) {
        // comparte en otras apps
        val intent = Intent(activity, Detalle::class.java)
        // start your next activity
        val preferencias = requireActivity().getSharedPreferences("variables", Context.MODE_PRIVATE)

        val editor = preferencias.edit()
        editor.putString("escaneo", "true")
        editor.commit()
        intent.putExtra("escaneo", "true")

        intent.putExtra("folio", rawResult.text)
        startActivity(intent)
        mScannerView!!.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()
    }

}
