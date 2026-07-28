package com.example.util

enum class AppLanguage(val code: String, val displayName: String, val flag: String) {
    RW("rw", "Kinyarwanda", "🇷🇼"),
    EN("en", "English", "🇬🇧"),
    SW("sw", "Kiswahili", "🇹🇿"),
    FR("fr", "Français", "🇫🇷")
}

object Localization {
    fun getString(lang: AppLanguage, key: String): String {
        return when (lang) {
            AppLanguage.RW -> rwStrings[key] ?: enStrings[key] ?: key
            AppLanguage.SW -> swStrings[key] ?: enStrings[key] ?: key
            AppLanguage.FR -> frStrings[key] ?: enStrings[key] ?: key
            AppLanguage.EN -> enStrings[key] ?: key
        }
    }

    private val enStrings = mapOf(
        "app_slogan" to "Pay people, not phone numbers.",
        "nav_hub" to "Hub",
        "nav_ai_pay" to "AI Pay",
        "nav_aura_sync" to "Aura Sync",
        "nav_memory" to "Memory",
        "nav_account" to "Account",
        "total_liquidity" to "TOTAL LIQUIDITY",
        "recent_activity" to "Recent Activity",
        "pay_via_ai" to "Pay via AI",
        "receive_money" to "Receive",
        "smart_routing_active" to "AI Smart Routing Active",
        "smart_routing_sub" to "Automatically optimizes route, fee & speed across MoMo, Bank & eCash.",
        "received_from" to "Received from",
        "paid_to" to "Paid to",
        "sent_to" to "Sent to",
        "notifications" to "Notifications",
        "no_notifications" to "No new notifications",
        "edit_credentials" to "Edit Credentials",
        "logout" to "Log Out",
        "sign_in" to "Sign In",
        "register" to "Register",
        "user_credentials" to "User Credentials & Linked Accounts",
        "security_ai_guardian" to "Security & AI Guardian",
        "default_nlu_language" to "Preferred System Language",
        "demo_data_control" to "Demo Data Control",
        "reset_demo_data" to "Reset Seed Data",
        "receive_qr_title" to "Your LINK Proximity & QR Badge",
        "receive_qr_sub" to "Show this QR code or use Aura Sync proximity to receive funds instantly.",
        "simulate_receive" to "Simulate Incoming Payment (Demo)"
    )

    private val rwStrings = mapOf(
        "app_slogan" to "Ishyura abantu, sibo nimero za telefoni.",
        "nav_hub" to "Ipaji Nkuru",
        "nav_ai_pay" to "Ishyura na AI",
        "nav_aura_sync" to "Kwiyunga (Aura)",
        "nav_memory" to "Ububiko",
        "nav_account" to "Konti Yanjye",
        "total_liquidity" to "UMUTUNGO WOSE WAWE",
        "recent_activity" to "Ibikorwa Bya Hafi",
        "pay_via_ai" to "Ishyura na AI",
        "receive_money" to "Yakira Amafaranga",
        "smart_routing_active" to "Inzira ya AI Yiteguye",
        "smart_routing_sub" to "Ihitamo inzira nziza, yihuta kandi ihendutse hagati ya MoMo, Banki na eCash.",
        "received_from" to "Yohererejwe na",
        "paid_to" to "Yishyuye",
        "sent_to" to "Yohererejwe",
        "notifications" to "Ibyitonderwa / Arerte",
        "no_notifications" to "Nta byitonderwa bishya",
        "edit_credentials" to "Hindura Imyirondoro",
        "logout" to "Sohoka muri Konti",
        "sign_in" to "Injira",
        "register" to "Iwandikishe",
        "user_credentials" to "Imyirondoro & Konti Zihujwe",
        "security_ai_guardian" to "Umutekano & Inzirabwoba AI",
        "default_nlu_language" to "Ururimi Rukoreshwa mu App",
        "demo_data_control" to "Ubugenzuzi bw'Igerageza",
        "reset_demo_data" to "Subiza ku Ntangiriro",
        "receive_qr_title" to "Inyandiko ya QR & Kode Yanjye",
        "receive_qr_sub" to "Yerekeze undi muntu cyangwa ukoreshe Kwiyunga (Aura Sync) kugira ngo yakire amafaranga.",
        "simulate_receive" to "Igerageza: Yakira Amafaranga (25,000 RWF)"
    )

    private val swStrings = mapOf(
        "app_slogan" to "Lipa watu, sio nambari za simu.",
        "nav_hub" to "Mwanzo",
        "nav_ai_pay" to "Lipa na AI",
        "nav_aura_sync" to "Aura Sync",
        "nav_memory" to "Kumbukumbu",
        "nav_account" to "Akaunti",
        "total_liquidity" to "JUMLA YA PHEDHA ZOTE",
        "recent_activity" to "Shughuli za Hivi Karibuni",
        "pay_via_ai" to "Lipa kwa AI",
        "receive_money" to "Pokea Pesa",
        "smart_routing_active" to "Njia Bora ya AI Ipo Wazi",
        "smart_routing_sub" to "Inachagua njia ya bei nafuu na ya haraka kupitia MoMo, Benki au eCash.",
        "received_from" to "Imepokelewa kutoka",
        "paid_to" to "Imelipwa kwa",
        "sent_to" to "Imetumwa kwa",
        "notifications" to "Arifa Zako",
        "no_notifications" to "Hakuna arifa mpya",
        "edit_credentials" to "Hariri Taarifa Zako",
        "logout" to "Ondoka",
        "sign_in" to "Ingia",
        "register" to "Sajili",
        "user_credentials" to "Taarifa za Akaunti Zilizounganishwa",
        "security_ai_guardian" to "Ulinzi wa AI & Usalama",
        "default_nlu_language" to "Lugha ya Mfumo",
        "demo_data_control" to "Usimamizi wa Onyesho",
        "reset_demo_data" to "Rudisha Mfumo",
        "receive_qr_title" to "Msimbo Wako wa QR wa Lipa",
        "receive_qr_sub" to "Onyesha msimbo huu au tumia Aura Sync kupokea fedha papo hapo.",
        "simulate_receive" to "Jaribu Kupokea Pesa (25,000 RWF)"
    )

    private val frStrings = mapOf(
        "app_slogan" to "Payez des personnes, pas des numéros.",
        "nav_hub" to "Accueil",
        "nav_ai_pay" to "Paiement AI",
        "nav_aura_sync" to "Aura Sync",
        "nav_memory" to "Historique",
        "nav_account" to "Mon Compte",
        "total_liquidity" to "LIQUIDITÉ TOTALE",
        "recent_activity" to "Activité Récente",
        "pay_via_ai" to "Payer via AI",
        "receive_money" to "Recevoir",
        "smart_routing_active" to "Routage Intelligent AI Actif",
        "smart_routing_sub" to "Optimise automatiquement les frais et la vitesse via MoMo, Banque et eCash.",
        "received_from" to "Reçu de",
        "paid_to" to "Payé à",
        "sent_to" to "Envoyé à",
        "notifications" to "Notifications",
        "no_notifications" to "Aucune nouvelle notification",
        "edit_credentials" to "Modifier les Identifiants",
        "logout" to "Se Déconnecter",
        "sign_in" to "Se Connecter",
        "register" to "S'inscrire",
        "user_credentials" to "Comptes Associés & Identifiants",
        "security_ai_guardian" to "Sécurité & Gardien AI",
        "default_nlu_language" to "Langue Principale de l'App",
        "demo_data_control" to "Contrôle des Données Démo",
        "reset_demo_data" to "Réinitialiser les Données",
        "receive_qr_title" to "Votre Badge QR & Proximité",
        "receive_qr_sub" to "Présentez ce QR code ou utilisez Aura Sync pour recevoir des fonds instantanément.",
        "simulate_receive" to "Simuler un Paiement Reçu (25 000 RWF)"
    )
}
