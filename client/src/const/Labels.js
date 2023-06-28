const Labels = {
    app: {
        name: 'Rozmer'
    },

    required: {
        all_req: 'All field are required',
        mandatory: 'Enter mandatory field'
    },

    alert: {
        info: 'Info',
        register: 'Register',
        reset_pass: 'Reset Password',
        logout: 'Logout',
        success: 'Success',
        error: 'Error',
        one_feed_away: 'Almost there! You\'re just one article away in getting lifetime free membership for the platform.',
        life_time_free: 'Congratulations! You now have lifetime free membership for the platform.',
        post_2_feed: 'Please publish 2 articles for free lifetime access to the platform.',
        confirm: 'Confirm'
    },

    login: {
        login_user: 'Email',
        password: 'Password',
        login: 'Login',
        newUser: 'Register as New User',
        forgetPassword: 'Forget Password?',
        failed: "The email address or phone number that you've entered doesn't match any account. <br/> <b>Sign up for an account.</b>",
        login_error: "Login Error",
        flash_msg: 'A new social media platform changing the meaning of news!'
    },

    register: {
        login_screen: 'Back to Login',
        verification_cnf: 'Verification link has been sent to the registered E-Mail address, click the link to verify',
        verification_err: 'E-Mail/Password field is not valid',
        validation: 'A field value has been left blank',
        pass_validation: 'Password/Confirm password must be same.',
        first_name: 'First Name',
        last_name: 'Last Name',
        mob_num: 'Mobile Number',
        email: 'Email',
        pass: 'Password',
        cnf_pass: 'Confirm Password',
        submit: 'Submit',
        user_verified: 'User verified',
        verification_exp: 'Verification code expired',
        verification_failed: 'verification failed,',
        continue: 'click on Login to continue',
        user: 'User',
        email_validation: 'Enter valid Email'
    },

    reset: {
        msg: 'Reset Password link has been sent to the registered E-Mail address, click the link to reset the password',
        flash_msg: 'A new social media platform changing the meaning of news!',
        body_msg: 'Lost your password? Please enter your email address. You will receive a link to create a new password via email.',
        confirm: 'New password has been updated successfully!!!',
        token_expired: 'Verification token expired',
        validation_msg: 'Password/Confirm password must be same.',
        flash_msg_confirm: 'A new social media platform changing the meaning of news!',
        new_pass: 'New Password',
        cnf_pass: 'Confirm Password',
        reset_pass_error: "Entered Email doesn't Exist"
    },

    dashboard: {
        logout_msg: 'Are you sure you want to Logout?',
        back: 'Back'
    },

    menu: {
        home: 'Feed',
        publish: 'Publish',
        profile: 'Profile',
        notification: 'Notification',
        logout: 'Logout'
    },

    header: {
        search: 'Search Post...'
    },

    profile: {
        no_post: '<p>No content published yet.</p><p>Publish 2 articles and get free life time access to the platform.</p>',
        published: 'Published',
        my_post: 'My Posts',
        edit_profile: 'Edit Profile',
        update_cnf: 'Profile Updated Successfully',
        update_fail: 'Failed to update User information',
        update: 'Update'
    },

    publish: {
        update_msg: 'Congratulations, Your changes have been successfully saved!',
        update_fail: 'Failed to save the changes',
        thumbnail_upload_fail: 'Failed to upload thumbnail image',
        create_err_title: 'Create Post Error',
        create_warn_title: 'Create Post Warning',
        title_validation: "Title Can't be empty",
        title_char_validation: 'Allowed character for title is minimum 3 and maximum 100 chars',
        content_validation: "Content cannot be empty!",
        thumbnail_validation: 'Looks like you forgot to add thumbnail image, do you want to add one? \n Please select any image from your device by clicking "Choose Thumbnail" option on top right.',
        yes: 'Yes',
        later: 'May be Later',
        delete_title: 'Delete Post',
        delete_cnf_body: 'Are you sure you want to delete the post?',
        del_success: 'Successfully deleted the Post',
        del_fail: 'Failed to delete Post',
        thumbnail_size_validation: 'Maximum allowed thumbnail size is 2MB',
        title: 'Title',
        choose_thumbnail: 'Choose Thumbnail',
        change_thumbnail: 'Change Thumbnail',
        delete: 'Delete',
        cancel: 'Cancel',
        update: 'Update',
        create: 'Create',
        preview: 'Preview',
        mob_info: 'For better experience use Desktop version'
    },

    loadingText: 'Loading...',

    list: {
        no_results: 'No Feed',
        comment_no_results: 'Be the first to post a comment',
        question_no_results: 'Be the first to post a question',
        comment_delete_title: 'Delete Comment?',
        comment_delete_confirm: 'Are you sure you want to delete this comment? You can\'t undo this action.',
        collaborate_title: 'Are you sure you want to collaborate this comment?'
    },

    question: {
        success: 'Your Question posted successfully',
        question_delete_title: 'Delete Question?',
        question_delete_confirm: 'Are you sure you want to delete this question? You can\'t undo this action and you will loose all the answers assigned.',
        answer_delete_title: 'Delete Answer?',
        answer_delete_confirm: 'Are you sure you want to delete this answer? You can\'t undo this action.',
        collaborate_title: 'Collaborate this question?',
        collaborate_confirm_msg: 'Are you sure you want to collaborate this question?'
    },

    details: {
        comment_title: 'Comments',
        question_title: 'Questions',
        rating_title: 'Rating',
        take_down_post: 'Take Down Post',
        submit_rating_popup: 'No takedown, just not cool 👎',
        rating_confirm_msg: "Are you sure you want to take down the post, if `Yes` please add your comment. <p>You can only takedown a post once per week the reason is we want to provide customers REAL control in return for REAL tolerance.</p>",
    },

    collaborate: {
        comment_title: 'Comment collaborated for the selected phrase',
        question_title: 'Q&A collaborated for the selected phrase',
        submit: 'Submit',
        add_success: 'Successfully Collaborated',
        collaborate_body: 'Please drag and select the content from the blog you want to collaborate',
        collaborate_title: 'Collaborate this comment?',
        collaborate_confirm_msg: 'Are you sure you want to collaborate this comment?',
        failed: 'Failed to load Details'
    },

    tip: {
        publish_fab: 'Publish a new post'
    },

    placeholder: {
        comments: "Type your comment here",
        question: "Type your question here",
        answer: "Type your answer here"
    }
};


export default Labels;