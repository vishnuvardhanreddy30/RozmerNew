<script>
    import { createEventDispatcher } from "svelte";
    import Utils from "../../util/Utils";
    import TextField from "../../widget/fields/TextField.svelte";
    import EmailField from "../../widget/fields/EmailField.svelte";
    import CheckBox from "../../widget/fields/CheckBox.svelte";
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
        pass,
        guestEmail,
        guestPassword,
        showLoginForm = true;

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
            ignoreCheck: true
        };

        Request.post(
            urlConst.login + Utils.encodeForUrl(data),
            data,
            onSuccess,
            onFailure,
            onSuccess
        );
    }

    function onGuestLogin() {
        let data = {
            email: guestEmail,
            password: "guest",
            ignoreCheck: true,
            role:"guest"
        };

        Request.post(
            urlConst.login + Utils.encodeForUrl(data),
            data,
            onSuccess,
            onFailure,
            onSuccess
        );
    }

    function onRegister() {
        Utils.redirectTo("register");
    }

    function onResetPassword() {
        Utils.redirectTo("reset");
    }

    function toggleForm() {
        showLoginForm = !showLoginForm;
    }
</script>

<div class="login-cont flex-cont wh-100-percent">
    <div class="login-left flex-1">
        <img src={logo} alt="" class="sub-logo" />
    </div>

    <div class="login-right flex-1 flex-cont flex-vh bg-standard">
        <div class="margin-bottom-to-child-15 p2 login-items">
            <img src={logo} alt="" class="sub-logo logo-hidden" />
            <div class="flex-cont space-around text-center">
                <p on:click={toggleForm} class="heading pointer btn-normal {showLoginForm? 'btn-active':''}">USER LOG IN</p>
                <p on:click={toggleForm} class="heading pointer btn-normal {!showLoginForm? 'btn-active':''}">GUEST LOG IN</p>
            </div>
            {#if showLoginForm}
                <EmailField
                    label={Labels.login.login_user}
                    {required}
                    placeholder="User name or Email*"
                    bind:value={name}
                />
                <PasswordField
                    label={Labels.login.password}
                    placeholder="Password*"
                    {required}
                    bind:value={pass}
                    on:enter={onLogin}
                />
                <Button
                text={Labels.login.login}
                on:click={onLogin}

                {fullWidth}
            />
            {:else}
                <!-- Guest Login Section -->

                <EmailField
                    label="Guest Email"
                    placeholder="guest@example.com"
                    bind:value={guestEmail}
                />
                <!-- <PasswordField
                    label="Guest Password"
                    placeholder="Guest Password"
                    bind:value={guestPassword}
                    on:enter={onGuestLogin}
                /> -->
                <Button
                text="Login"
                on:click={onGuestLogin}

                {fullWidth}
            />

            {/if}
            <div class="flex-cont space-between margin-top-20">
                <CheckBox label={Labels.login.remeber}/>
                <div class="">
                    <div class="text-right">
                        <span class="register-link color-white" on:click={onResetPassword}
                            >{Labels.login.forgetPassword}</span
                        >
                    </div>
                    <div class="text-right">
                        <span class="register-link" on:click={onRegister}
                            >{Labels.login.newUser}</span
                        >
                    </div>
                </div>
            </div>

        </div>

        <p class="copy-text text-right">Copyright &copy; 2023 Rozmer. All rights reserved.</p>

    </div>
</div>
