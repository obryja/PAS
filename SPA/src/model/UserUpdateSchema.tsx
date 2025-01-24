import * as Yup from 'yup';

export const userUpdateSchema = Yup.object().shape({
    password: Yup.string()
        .min(3, 'Hasło musi mieć co najmniej 3 znaki.')
        .max(50, 'Hasło nie może przekroczyć 50 znaków.')
        .required('Hasło jest wymagane.'),
});