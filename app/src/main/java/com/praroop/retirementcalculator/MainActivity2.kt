package com.praroop.retirementcalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import org.w3c.dom.Text
import kotlin.math.pow

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        AppCenter.start(
            application,
            "3cbb9542-945b-4561-a0cf-5e4595239c6e",
            Analytics::class.java,
            Crashes::class.java
        )
        var calculateButton = findViewById<Button>(R.id.calculateButton)
        var interestEditText = findViewById<EditText>(R.id.interestEditText)
        var ageEditText = findViewById<EditText>(R.id.ageEditText)
        var retirementEditText = findViewById<EditText>(R.id.retirementEditText)
        var monthlySavingsEditText = findViewById<EditText>(R.id.monthlySavingsEditText)
        var currentEditText = findViewById<EditText>(R.id.currentEditText)
        var resultTextView = findViewById<TextView>(R.id.resultTextView)
        calculateButton.setOnClickListener {

            try {
                val interestRate = interestEditText.text.toString().toFloat()
                val currentAge = ageEditText.text.toString().toInt()
                val retirementAge = retirementEditText.text.toString().toInt()
                val monthly = monthlySavingsEditText.text.toString().toFloat()
                val current = currentEditText.text.toString().toFloat()

                val properties:HashMap<String, String> = HashMap<String, String>()
                properties.put("interest_rate", interestRate.toString())
                properties.put("current_age", currentAge.toString())
                properties.put("retirement_age", retirementAge.toString())
                properties.put("monthly_savings", monthly.toString())
                properties.put("current_savings", current.toString())
                if (interestRate <= 0) {
                    Analytics.trackEvent("wrong_interest_rate", properties)
                }
                if (retirementAge <= currentAge) {
                    Analytics.trackEvent("wrong_age",properties)
                }
                val futureSavings = calculateRetirement(interestRate, current, monthly, (retirementAge - currentAge)*12)

                resultTextView.text = "At the current rate of $interestRate%, saving \$$monthly a month you will have \$${String.format("%f", futureSavings)} by $retirementAge."
            } catch (e: Exception) {
                Analytics.trackEvent(e.message)
            }

//            throw Exception("SomeThing Wrong happend")
            //Crashes.generateTestCrash()
        }

    }

    fun calculateRetirement(interestRate: Float, currentSavings: Float, monthly: Float, numMonths: Int): Float {
        var futureSavings = currentSavings * (1+(interestRate/100/12)).pow(numMonths)

        for (i in 1..numMonths) {
            futureSavings += monthly * (1+(interestRate/100/12)).pow(i)
        }

        return  futureSavings
    }
}