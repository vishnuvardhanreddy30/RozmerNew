<script>
    import { fly } from "svelte/transition";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import Utils from "../../util/Utils";
    import TextField from "../../widget/fields/TextField.svelte";
    import PasswordField from "../../widget/fields/PasswordField.svelte";
    import NumberField from "../../widget/fields/NumberField.svelte";
    import Labels from "../../const/Labels";
    import Request from "../../util/Request";
    import urlConst from "../../const/Url";

    import logo from "../../assets/logo.png";

    let mobNumber, email, pass, cpass, fname, lname;

    let required = true,
        fullWidth = true,
        maxLength = 10;

    function onBack() {
        Utils.redirectBack();
    }

    function onSuccess(res) {
        Utils.alert(
            Labels.register.verification_cnf,
            Labels.alert.register,
            onBack
        );
        Utils.mask();
    }

    function onFailure(err) {
        Utils.alert(
            Labels.register.verification_err,
            Labels.alert.register,
            () =>{}
        );

        Utils.mask();
    }
    function onSubmit() {
        if (
            Utils.isEmpty(fname) ||
            Utils.isEmpty(lname) ||
            Utils.isEmpty(mobNumber) ||
            Utils.isEmpty(email) ||
            Utils.isEmpty(pass) ||
            Utils.isEmpty(cpass)
        ) {
            Utils.alert(Labels.register.validation, Labels.alert.register);
            return;
        }

        if (pass !== cpass) {
            Utils.alert(Labels.register.pass_validation, Labels.alert.register);
            return;
        }

        if (!Utils.isValidEmail(email)) {
            Utils.alert(
                Labels.register.email_validation,
                Labels.alert.register
            );
            return;
        }

        Utils.mask(true);
        Request.post(
            urlConst.register,
            {
                email: email,
                firstName: fname,
                lastName: lname,
                mobileNo: mobNumber,
                password: pass,
                username: fname + " " + lname,
                ignoreCheck: true,
            },
            onSuccess,
            onFailure,
            onSuccess
        );
    }
</script>

<div
    class="main-sub-page wh-100-percent"
    transition:fly={{ x: window.innerWidth, duration: 500 }}
>
    <!-- <Toolbar cls="theme-bg">
        <div slot="left">
            <Button
                text="Register"
                iconCls="material-icons"
                iconText="keyboard_backspace"
                on:click={onBack}
            />
        </div>
    </Toolbar> -->

    <div class="login-cont flex-cont wh-100-percent">
        <!-- <div class="wh-100-percent flex-1 flex-cont flex-vh"> -->
        <div class="login-left bg-gradient flex-1">
            <!-- <div class="login-title-cont">
                A new social media platform changing the meaning of news!
            </div> -->
        </div>
        <div class="login-right flex-1 flex-cont flex-vh">
            <div class="margin-bottom-to-child-15 p3 login-items">
                <img src={logo} alt="" class="sub-logo" />
                <TextField
                    {required}
                    label={Labels.register.first_name}
                    bind:value={fname}
                />
                <TextField
                    {required}
                    label={Labels.register.last_name}
                    bind:value={lname}
                />
                <!-- <TextField
                        {required}
                        label="User Name"
                        bind:value={username}
                    /> -->
                <NumberField
                    {required}
                    label={Labels.register.mob_num}
                    bind:value={mobNumber}
                    {maxLength}
                />
                <TextField
                    {required}
                    label={Labels.register.email}
                    bind:value={email}
                />
                <PasswordField
                    {required}
                    label={Labels.register.pass}
                    bind:value={pass}
                />
                <PasswordField
                    {required}
                    label={Labels.register.cnf_pass}
                    bind:value={cpass}
                />

                <Toolbar ui="plain">
                    <Button
                        text={Labels.register.submit}
                        on:click={onSubmit}
                        slot="center"
                        {fullWidth}
                    />
                </Toolbar>

                <div class="txt-center">
                    <span class="register-link" on:click={onBack}
                        >{Labels.register.login_screen}</span
                    >
                </div>
            </div>
        </div>
    </div>
</div>
