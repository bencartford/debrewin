from setup_etc import DebSequence
deb = DebSequence


# note: in order to run these, you will need to have pytest installed to your local machine.
#   how to install pytest:
#       in terminal: pip install pytest
#       if you get error: pip not installed (or something), do 'sudo easy_install pip'
#       then go back and do pip install pytest

def test_is_deb_seq():
    sec = '0110'
    wind = 2
    deb.is_deb_seq(sec, wind)
    assert True


def test_shuffle_seq_when_seq_is_even():
    seq = '123456'
    shuffled_sequence = deb.shuffle_seq(seq)
    assert shuffled_sequence == '142536'


def test_shuffle_seq_when_seq_is_odd():
    seq = '12345'
    shuffled_sequence = deb.shuffle_seq(seq)
    assert shuffled_sequence == '14253'


def test_shift_the_sequence():
    seq = '123456'
    shifted_sequence = deb.shift_the_sequence(seq)
    assert shifted_sequence == '612345'


def test_append_list_of_total_seq():
    deb.append_list_of_total_seq(['0', '1'], 1)
    pass


def test_make_unique_window_sequences():
    window_size = 2
    arr = [None] * 2
    current_size = 0
    alphabet_array = ['0', '1']
    deb.make_unique_window_sequences(window_size, arr, current_size, alphabet_array)
    pass



