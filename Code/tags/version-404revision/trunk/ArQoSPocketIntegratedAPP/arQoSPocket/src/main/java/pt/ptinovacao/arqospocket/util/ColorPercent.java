package pt.ptinovacao.arqospocket.util;

import android.content.res.Resources;

import pt.ptinovacao.arqospocket.R;


public class ColorPercent {
    int color,  image, percent;

    public void TestType(int testType, int position) {
        switch (testType) {
            // falta o 0%

            case R.string.test_inactive:
                teste_inactivo(position);
                break;
            case R.string.test_running:
                teste_em_curso(position);
                teste_em_curso(position);
                teste_em_curso(position);
                break;
            case R.string.test_completed:
                teste_finalizado(position);
                break;
            case R.string.test_failed:
                teste_falhado(position);
                teste_falhado(position);
                teste_falhado(position);
                break;
            default:

                break;
        }
    }

    public void teste_em_curso(int percentagem) {
        //Resources res = getResources();
        //String text = String.format(res.getString(R.string.task_percent), percentagem);
        //percent = String.format(text percentagem);

        switch (percentagem) {
            // falta o 0%

            case 0:
                image = R.drawable.falhado_default;
                percent = R.string.percent_0;
                color = R.color.em_curso;
                break;

            case 10:

                image = R.drawable.em_curso_10;
                percent = R.string.percent_10;
                color = R.color.em_curso;
                break;
            case 20:

                image = R.drawable.em_curso_20;
                percent = R.string.percent_20;
                color = R.color.em_curso;
                break;
            case 30:

                image = R.drawable.em_curso_30;
                percent = R.string.percent_30;
                color = R.color.em_curso;
                break;
            case 40:

                image = R.drawable.em_curso_40;
                percent = R.string.percent_40;
                color = R.color.em_curso;
                break;
            case 50:

                image = R.drawable.em_curso_50;
                percent = R.string.percent_50;
                color = R.color.em_curso;
                break;
            case 60:
                image = R.drawable.em_curso_60;
                percent = R.string.percent_60;
                color = R.color.em_curso;
                break;
            case 70:
                image = R.drawable.em_curso_70;

                break;
            case 80:
                image = R.drawable.em_curso_80;
                percent = R.string.percent_80;
                color = R.color.em_curso;
                break;
            case 90:
                image = R.drawable.em_curso_90;
                percent = R.string.percent_90;
                color = R.color.em_curso;
                break;
            case 100:
                image = R.drawable.em_curso_100;
                percent = R.string.percent_100;
                color = R.color.em_curso;
                break;
            default:

                break;
        }
    }

    public void teste_falhado(int percentagem) {
        switch (percentagem) {
            case 10:
                image = R.drawable.falhado_10;
                percent = R.string.percent_10;
                color = R.color.falhado;
                break;
            case 20:
                image = R.drawable.falhado_20;
                percent = R.string.percent_20;
                color = R.color.falhado;
                break;
            case 30:
                image = R.drawable.falhado_30;
                percent = R.string.percent_30;
                color = R.color.falhado;
                break;
            case 40:
                image = R.drawable.falhado_40;
                percent = R.string.percent_40;
                color = R.color.falhado;
                break;
            case 50:
                image = R.drawable.falhado_50;
                percent = R.string.percent_50;
                color = R.color.falhado;
                break;
            case 60:
                image = R.drawable.falhado_60;
                percent = R.string.percent_60;
                color = R.color.falhado;
                break;
            case 70:
                image = R.drawable.falhado_70;
                percent = R.string.percent_70;
                color = R.color.falhado;
                break;
            case 80:
                image = R.drawable.falhado_80;
                percent = R.string.percent_80;
                color = R.color.falhado;
                break;
            case 90:
                image = R.drawable.falhado_90;
                percent = R.string.percent_90;
                color = R.color.falhado;
                break;
            case 100:
                image = R.drawable.falhado_100;
                percent = R.string.percent_100;
                color = R.color.falhado;
                break;
            default:
                image = R.drawable.falhado_100;
                percent = R.string.percent_0;
                color = R.color.falhado;
                break;
        }
    }

    public void teste_finalizado(int percentagem) {
        switch (percentagem) {


            case 100:

                image = R.drawable.concluido_100;
                percent = R.string.percent_100;
                color = R.color.concluido;
                break;

            default:

                break;
        }
    }

    public void teste_inactivo(int percentagem) {
        switch (percentagem) {

            case 0:
                image = R.drawable.falhado_default;
                percent = R.string.percent_0;
                color = R.color.inactivo;
                break;

            default:

                break;
        }
    }

    public int getColor() {
        return color;
    }

    public int getpercent() {
        return percent;
    }

    public int getimage() {
        return image;
    }
}
