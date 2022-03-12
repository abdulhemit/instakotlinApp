package com.example.ilkacilma.profile


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth


class SingOutFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var alert = AlertDialog.Builder(requireContext())
            .setTitle("Instagram'dan çıkış Yap")
            .setMessage("Emin minisiz ?")
            .setPositiveButton("çıkış Yap",object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {

                    FirebaseAuth.getInstance().signOut()
                    activity?.finish()
                }

            })
            .setNegativeButton("Iptal",object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {

                    dismiss()
                }

            })
            .create()

        return alert
    }
}