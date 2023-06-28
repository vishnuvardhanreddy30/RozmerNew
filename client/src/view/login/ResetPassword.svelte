<script>
    import { fly } from "svelte/transition";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import TextField from "../../widget/fields/TextField.svelte";
    import Request from "../../util/Request";
    import urlConst from "../../const/Url";
    import Utils from "../../util/Utils";
    import Labels from "../../const/Labels";
    import logo from "../../assets/logo.png";

    let fullWidth = true,
        required = true,
        email;

    function onBack() {
        Utils.redirectBack();
    }

    function onSuccess(res) {
        Utils.alert(Labels.reset.msg, Labels.alert.reset_pass, onBack);

        Utils.mask();
        Utils.log(res);
    }

    function onFailure(err) {
        Utils.mask();
        Utils.alert(Labels.reset.reset_pass_error, Labels.alert.reset_pass);
        Utils.log(err);
    }

    function onSubmit() {
        if (Utils.isEmpty(email)) {
            Utils.alert(Labels.required.mandatory, Labels.alert.error);
            return;
        }

        if (!Utils.isValidEmail(email)) {
            Utils.alert(
                Labels.register.email_validation,
                Labels.alert.reset_pass
            );
            return;
        }

        Utils.mask(true);

        let data = { email: email, ignoreCheck: true };

        Request.post(
            urlConst.forgot_password + Utils.encodeForUrl(data),
            data,
            onSuccess,
            onFailure,
            onSuccess
        );
    }
</script>

<div class="login-cont flex-cont wh-100-percent">
    <div class="login-left bg-gradient flex-1">
        <!-- <div class="login-title-cont">
            {Labels.reset.flash_msg}
        </div> -->
    </div>

    <div class="login-right flex-1 flex-cont flex-vh">
        <div class="margin-bottom-to-child-15 p3 login-items">
            <img src={logo} alt="" class="sub-logo" />
            <div class="reset-pass-desc">
                {Labels.reset.body_msg}
            </div>
            <TextField {required} label="Email" bind:value={email} />

            <Toolbar ui="plain">
                <Button
                    text="Submit"
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

<style>
    .reset-pass-desc {
        max-width: 260px;
        font-size: 0.9rem;
        text-align: justify;
    }
</style>
