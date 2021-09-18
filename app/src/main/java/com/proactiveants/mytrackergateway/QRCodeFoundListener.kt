package com.proactiveants.mytrackergateway

interface QRCodeFoundListener {
    fun onQRCodeFound(qrCode: String?)
    fun qrCodeNotFound()
}