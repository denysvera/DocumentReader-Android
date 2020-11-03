package com.regula.documentreader.ui.main


import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.regula.documentreader.R
import com.regula.documentreader.api.DocumentReader
import com.regula.documentreader.api.completions.IDocumentReaderCompletion
import com.regula.documentreader.api.enums.DocReaderAction
import com.regula.documentreader.api.enums.Scenario
import com.regula.documentreader.api.enums.eGraphicFieldType
import kotlinx.android.synthetic.main.back_document_result_card.view.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.scan_document_step_one_card.view.*

class ScanFragment : Fragment() {

    companion object {
        fun newInstance() = ScanFragment()
    }

    private lateinit var viewModel: MainViewModel
    var isFirstDocumentScanned = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
        DocumentReader.Instance().processParams().scenario = Scenario.SCENARIO_OCR
        layoutStepOne.openScannerButton.setOnClickListener {
            scanIdentificationDocument()
        }
        layoutNoInsuranceCard.openScannerButton.setOnClickListener {
            scanIdentificationDocument()
        }

    }

    private val completion = IDocumentReaderCompletion { action, results, error ->

        // processing is finished, all results are ready
        if (action == DocReaderAction.COMPLETE) {
            if(results.documentType.size ==0 ){

            }else {
                    // val scanDoc = SupportedDocument(results.documentType.first().name!!,results.documentType.first().dType)
                   // DocumentReader.Instance().stopScanner(requireContext())
                    layoutStepOne.visibility = View.GONE
                    val bitmapImage =
                        results.getGraphicFieldImageByType(eGraphicFieldType.GF_DOCUMENT_IMAGE)

                        showBackDocumentCard()
                        if (isFirstDocumentScanned){
                            frontImage.setImageBitmap(bitmapImage)
                            identityDocCheckbox.isChecked = true
                        }else{
                            layoutBackCard.backImageView.setImageBitmap(bitmapImage)
                            insuranceImage.setImageBitmap(bitmapImage)
                            insuranceCheckBox.isChecked = true
                            isFirstDocumentScanned = true
                        }


                   // viewModel.regulaDocument = results
                    // this.findNavController().navigate(R.id.action_documentScanFragment_to_customerSearchResultsFragment)

                }


        } else {
            if (action == DocReaderAction.CANCEL) {
                Toast.makeText(requireActivity(), "Scanning was cancelled", Toast.LENGTH_LONG).show()
            } else if (action == DocReaderAction.ERROR) {
                //binding.captureImage.setImageBitmap(results.getGraphicFieldImageByType(eGraphicFieldType.GF_DOCUMENT_IMAGE))
                Toast.makeText(requireActivity(), "Error:$error", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun scanIdentificationDocument() {
        DocumentReader.Instance().showScanner(requireActivity(), completion)
    }
    private fun showBackDocumentCard() {
        layoutBackCard.visibility = View.VISIBLE
        layoutBackCard.backDocumentRescanButton.setOnClickListener {
            scanIdentificationDocument()
        }
        layoutBackCard.backDocumentSkipButton.visibility = View.GONE


        layoutBackCard.backDocumentTakePicButton.text = "Scan ID Document"
        layoutBackCard.backDocumentTakePicButton.setOnClickListener {
            layoutBackCard.visibility = View.GONE


            layoutStepOne.visibility = View.VISIBLE
           // binding.layoutNoInsuranceCard.root.visibility = View.GONE
        }
        layoutBackCard.instructions.text= "Continue by scanning Swiss Passport or Swiss National ID or Foreign Passport"

    }
}