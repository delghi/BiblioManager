



function verificaRegUtente()
{
    /*if (document.form.radio.value!='accetta'){
alert("devi accettare il contratto per iscriverti");
return;
}*/


    //Controlla la presenza dei campi nome e cognome
    if (document.signupform.nome_reg.value == "")
    {
        alert("il campo nome e' obbligatorio");
        document.signupform.nome_reg.focus();
        return false;
    }
        
    if (document.signupform.cognome_reg.value == "")
    {
        alert("il campo cognome e' obbligatorio");
        document.signupform.cognome_reg.focus();
        return false;
    }
    
    
    
   if (document.signupform.data.value == "")
    {
        alert("il campo anno e' obbligatorio");
        document.signupform.data.focus();
        return false;
    } 
    
    
    
    
        


    if (document.signupform.password_reg.value.length < 4 )
    {
        alert("inserire una password di almeno 4 caratteri");
        document.signupform.password_reg.focus();
        return false;
    }
    



    var stato=true;


    if(document.signupform.email_reg.value.indexOf(" ")!=-1) {
        document.signupform.email_reg.focus();
        stato=false;
    }

    var chiocciola=document.signupform.email_reg.value.indexOf("@");
    if(chiocciola<2) {
        document.signupform.email_reg.focus();
        stato=false;
    }

    var punto=document.signupform.email_reg.value.indexOf(".", chiocciola);
    if(punto<chiocciola+3) {
        document.signupform.email_reg.focus();
        stato=false;
    }

    var lung=document.signupform.email_reg.value.length;
    if(lung-punto<3) {
        document.signupform.email_reg.focus();
        stato=false;
    }

    if(stato==false){
        alert("E-mail non valida");
        document.signupform.email_reg.focus();
        return false;
    }

    document.signupform.action="reg";
    return true;
}



