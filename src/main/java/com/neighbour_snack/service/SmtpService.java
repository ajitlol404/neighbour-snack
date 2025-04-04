package com.neighbour_snack.service;

import com.neighbour_snack.dto.SmtpDTO;
import com.neighbour_snack.dto.SmtpDTO.SmtpResponseDTO;

public interface SmtpService {

    SmtpResponseDTO getSmtp();

    void testSmtpConfiguration(SmtpDTO.SmtpRequestDTO request);

    SmtpResponseDTO updateSmtp(SmtpDTO.SmtpRequestDTO request);

}
