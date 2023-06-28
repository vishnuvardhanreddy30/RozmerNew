<script>
    import { createEventDispatcher } from "svelte";
    import Utils from "../../util/Utils";

    import TextField from "../../widget/fields/TextField.svelte";
    import PasswordField from "../../widget/fields/PasswordField.svelte";
    import Toolbar from "../../widget/toolbar/Toolbar.svelte";
    import Button from "../../widget/button/Button.svelte";
    import Labels from "../../const/Labels";
    import Request from "../../util/Request";
    import urlConst from "../../const/Url";
    import SessionUtil from "../../util/SessionUtil";

    import logo from "../../assets/logo.png";

    const dispatch = createEventDispatcher();
    let required = true,
        fullWidth = true,
        name,
        pass;

    function onSuccess(resp) {
        Utils.log(resp);

        if (Utils.isEmpty(resp)) {
            SessionUtil.removeAll();

            return onFailure();
        }

        SessionUtil.set("info", resp);
        dispatch("changeview", "home");
    }

    function onFailure(err) {
        Utils.alert(Labels.login.failed, Labels.login.login_error, null, {
            textBodyCls: "flex-dir-column",
        });
    }

    const onLogin = () => {
        if (Utils.isEmpty(name) || Utils.isEmpty(pass)) {
            return Utils.alert(Labels.required.all_req, Labels.alert.info);
        }

        let data = {
            email: name,
            password: pass,
            ignoreCheck: true,
        };

        Request.post(
            urlConst.login + Utils.encodeForUrl(data),
            data,
            onSuccess,
            onFailure,
            onSuccess
        );
    };

    function onRegister() {
        // dispatch("changeview", "register");
        Utils.redirectTo("register");
    }

    function onResetPassword() {
        Utils.redirectTo("reset");
    }
</script>

<div class="login-cont flex-cont wh-100-percent">
    <div class="login-left bg-gradient flex-1">
        <!-- <div class="login-title-cont">
            {Labels.login.flash_msg}
        </div> -->
    </div>

    <div class="login-right flex-1 flex-cont flex-vh">
        <div class="margin-bottom-to-child-15 p3 login-items">
            <img src={logo} alt="" class="sub-logo" />
            <TextField
                label={Labels.login.login_user}
                {required}
                bind:value={name}
            />
            <PasswordField
                label={Labels.login.password}
                {required}
                bind:value={pass}
                on:enter={onLogin}
            />

            <Toolbar ui="plain">
                <Button
                    text={Labels.login.login}
                    on:click={onLogin}
                    slot="center"
                    {fullWidth}
                />
            </Toolbar>

            <div class="txt-center">
                <span class="register-link" on:click={onRegister}
                    >{Labels.login.newUser}</span
                >
            </div>

            <div class="txt-center">
                <span class="register-link" on:click={onResetPassword}
                    >{Labels.login.forgetPassword}</span
                >
            </div>
        </div>
    </div>
</div>
